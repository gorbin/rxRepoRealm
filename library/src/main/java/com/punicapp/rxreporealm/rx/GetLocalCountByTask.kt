package com.punicapp.rxreporealm.rx

import com.punicapp.rxrepocore.AbstractQuery
import com.punicapp.rxrepocore.LocalFilters

import io.realm.RealmObject


class GetLocalCountByTask<T : RealmObject>(type: Class<T>, filters: LocalFilters?) : AbstractGetLocalDataTask<Long, T>(type, filters, null) {

    override fun processResults(query: AbstractQuery<T>): Long {
        return query.count()
    }
}
