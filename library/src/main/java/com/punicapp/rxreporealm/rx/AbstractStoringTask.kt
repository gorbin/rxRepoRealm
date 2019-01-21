package com.punicapp.rxreporealm.rx

import io.reactivex.functions.Consumer
import io.realm.Realm
import io.realm.RealmObject

abstract class AbstractStoringTask<T>(private val type: Class<out RealmObject>) : Consumer<T> {
    var removeBeforeSave = true
    var updateData = false

    protected abstract fun putDataInRealm(realm: Realm, t: T, updateData: Boolean)

    override fun accept(t: T) {
        val instance = Realm.getDefaultInstance()
        try {
            instance.beginTransaction()
            if (removeBeforeSave) instance.delete(type)
            putDataInRealm(instance, t, updateData)
        } finally {
            instance.commitTransaction()
            instance.close()
        }
    }
}
