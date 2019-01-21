package com.punicapp.rxreporealm.rx


import io.realm.Realm
import io.realm.RealmObject

class StoringDataTask<T : RealmObject>(type: Class<out RealmObject>) : AbstractStoringTask<T>(type) {

    override fun putDataInRealm(realm: Realm, t: T, updateData: Boolean) {
        if (updateData)
            realm.copyToRealmOrUpdate(t)
        else
            realm.copyToRealm(t)
    }
}
