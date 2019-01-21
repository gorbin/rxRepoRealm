package com.punicapp.rxreporealm

import com.google.common.base.Optional
import com.punicapp.rxrepocore.IRepository
import com.punicapp.rxrepocore.LocalFilters
import com.punicapp.rxrepocore.LocalSorts
import com.punicapp.rxreporealm.rx.*
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.realm.RealmObject

class DataRepository<T : RealmObject>(private val clazz: Class<T>) : IRepository<T> {

    override fun saveInChain(clearData: Boolean): Consumer<T> {
        return StoringDataTask<T>(clazz).apply {
            removeBeforeSave = clearData
            updateData = !clearData
        }
    }

    override fun saveAllInChain(clearData: Boolean): Consumer<List<T>> {
        return StoringListDataTask<T>(clazz).apply {
            removeBeforeSave = clearData
            updateData = !clearData
        }
    }

    override fun modifyFirst(action: Consumer<T>): Single<T> {
        return first(LocalFilters(), LocalSorts())
                .map { tWrapper ->
                    if (!tWrapper.isPresent) {
                        throw IllegalStateException("Modified object is null!!!")
                    }
                    tWrapper.get()
                }
                .doOnSuccess(action)
                .doOnSuccess(saveInChain())
    }

    override fun removeInChain(filters: LocalFilters?): Single<Int> {
        return Single.fromCallable(RemovingDataTask(clazz, filters))
    }

    override fun fetch(filters: LocalFilters?, sorts: LocalSorts?): Single<Optional<List<T>>> {
        return Single.fromCallable(GetLocalDataByTask(clazz, filters, sorts))
    }

    override fun first(filters: LocalFilters?, sorts: LocalSorts?): Single<Optional<T>> {
        return Single.fromCallable(GetLocalDataSingleTask(clazz, filters, sorts))
    }

    override fun count(filters: LocalFilters?): Single<Long> {
        return Single.fromCallable(GetLocalCountByTask(clazz, filters))
    }
}
