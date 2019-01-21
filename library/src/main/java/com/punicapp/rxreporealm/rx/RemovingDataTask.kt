package com.punicapp.rxreporealm.rx

import com.punicapp.rxrepocore.AbstractQuery
import com.punicapp.rxrepocore.LocalFilter
import com.punicapp.rxrepocore.LocalFilters

import io.realm.RealmObject

class RemovingDataTask<T : RealmObject>(type: Class<T>, filters: LocalFilters?) : AbstractGetLocalDataTask<Int, T>(type, filters, null) {

    override fun processResults(query: AbstractQuery<T>): Int {
        return query.remove()
    }
}
