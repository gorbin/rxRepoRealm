package com.punicapp.rxreporealm.rx

import com.google.common.base.Optional
import com.punicapp.rxrepocore.AbstractQuery
import com.punicapp.rxrepocore.LocalFilters
import com.punicapp.rxrepocore.LocalSorts

import io.realm.RealmObject


class GetLocalDataSingleTask<T : RealmObject>(type: Class<T>, filters: LocalFilters?, sorts: LocalSorts?) : AbstractGetLocalDataTask<Optional<T>, T>(type, filters, sorts) {

    override fun processResults(query: AbstractQuery<T>): Optional<T> {
        return Optional.fromNullable(query.first())
    }
}
