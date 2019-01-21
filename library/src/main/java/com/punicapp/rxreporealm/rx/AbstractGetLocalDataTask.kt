package com.punicapp.rxreporealm.rx

import com.punicapp.rxrepocore.AbstractQuery
import com.punicapp.rxrepocore.LocalFilters
import com.punicapp.rxrepocore.LocalSorts
import com.punicapp.rxreporealm.RealmRepoQuery
import io.realm.Realm
import io.realm.RealmObject
import java.util.concurrent.Callable

abstract class AbstractGetLocalDataTask<T, TR : RealmObject>(protected var type: Class<TR>, protected var filters: LocalFilters?, protected var sorts: LocalSorts?) : Callable<T> {

    @Throws(Exception::class)
    override fun call(): T {
        val queryBuilder = RealmRepoQuery(type)

        filters?.filters?.apply(queryBuilder::filter)
        sorts?.sorts?.apply(queryBuilder::sort)

        val instance = Realm.getDefaultInstance()
        queryBuilder.setRealmInstance(instance)
        return processResults(queryBuilder)
    }

    internal abstract fun processResults(query: AbstractQuery<TR>): T
}
