package com.punicapp.rxreporealm.rx

import com.google.common.base.Optional
import com.punicapp.rxrepocore.AbstractQuery
import com.punicapp.rxrepocore.LocalFilters
import com.punicapp.rxrepocore.LocalSorts

import io.realm.RealmObject


class GetLocalDataByTask<T : RealmObject>(type: Class<T>, filters: LocalFilters?, sorts: LocalSorts?) : AbstractGetLocalDataTask<Optional<List<T>>, T>(type, filters, sorts) {

    override fun processResults(query: AbstractQuery<T>): Optional<List<T>> {
        return Optional.fromNullable(query.find())
    }
}
