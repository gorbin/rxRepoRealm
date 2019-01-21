package com.punicapp.rxreporealm.rx


import io.realm.Realm
import io.realm.RealmObject

class StoringListDataTask<T : RealmObject>(type: Class<out RealmObject>) : AbstractStoringTask<List<T>>(type) {

    override fun putDataInRealm(realm: Realm, ts: List<T>, updateData: Boolean) {
        if (updateData)
            realm.copyToRealmOrUpdate(ts)
        else
            realm.copyToRealm(ts)
    }
}
