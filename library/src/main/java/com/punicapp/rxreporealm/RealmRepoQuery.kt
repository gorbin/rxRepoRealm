package com.punicapp.rxreporealm

import android.util.Pair
import com.punicapp.rxrepocore.AbstractQuery
import com.punicapp.rxrepocore.Check
import com.punicapp.rxrepocore.LocalFilter
import com.punicapp.rxrepocore.SortDir
import io.realm.*
import java.util.*

internal class RealmRepoQuery<out T : RealmObject>(private val clazz: Class<T>) : AbstractQuery<T>() {
    private lateinit var realmInstance: Realm

    private val sortParameters: Pair<Array<String>, Array<Sort>>?
        get() {
            return sortParamesGetterCalc()
        }

    private fun sortParamesGetterCalc(): Pair<Array<String>, Array<Sort>>? {
        if (sorting.size == 0) {
            return null
        }
        val property = ArrayList<String>()
        val sorts = ArrayList<Sort>()

        sorting.forEach {
            property.add(it.property)
            sorts.add(if (it.sort == SortDir.Asc) Sort.ASCENDING else Sort.DESCENDING)
        }
        val fieldNames = property.toTypedArray()
        val sortOrders = sorts.toTypedArray()
        return Pair(fieldNames, sortOrders)
    }

    fun setRealmInstance(realmInstance: Realm) {
        this.realmInstance = realmInstance
    }

    override fun find(): List<T> {
        val sortParameters = sortParameters
        val realmQuery = buildRealmQuery(realmInstance)
        val results: RealmResults<T>
        results = if (sortParameters != null)
            realmQuery.findAllSorted(sortParameters.first, sortParameters.second)
        else
            realmQuery.findAll()
        return realmInstance.copyFromRealm(results)
    }

    override fun first(): T? {
        val sortParameters = sortParameters

        val query = buildRealmQuery(realmInstance)
        var first: T? = if (sortParameters != null)
            query.findAllSorted(sortParameters.first, sortParameters.second)[0]
        else {
            query.findFirst()
        }

        return first?.let {realmInstance.copyFromRealm(it)}
    }

    override fun count(): Long {
        return buildRealmQuery(realmInstance).count()
    }

    override fun remove(): Int {
        realmInstance.beginTransaction()
        val all = buildRealmQuery(realmInstance).findAll()
        val size = all.size
        val success = all.deleteAllFromRealm()
        realmInstance.commitTransaction()
        return if (success) size else 0
    }

    private fun buildRealmQuery(instance: Realm?): RealmQuery<T> {
        var query = instance!!.where(clazz)

        filters.forEach {
            query = applyFilter(query, it)
        }

        return query
    }

    private fun applyFilter(query: RealmQuery<T>, filter: LocalFilter): RealmQuery<T> {
        val check = filter.check
        val prop = filter.idProp
        val value = filter.value
        when (check) {
            Check.Equal -> handleEq(query, prop, value)
            Check.NotEqual -> handleNeq(query, prop, value)
            Check.GreatOrEqual -> handleGorEq(query, prop, value)
            Check.LowerOrEqual -> handleLorEq(query, prop, value)
            Check.IsNull -> query.isNull(prop)
            Check.IsNotNull -> query.isNotNull(prop)
            Check.In -> handleIn(query, prop, value as Array<*>)
            Check.Contains, Check.ContainsIgnoreCase -> {
                val case = if (check == Check.Contains) Case.SENSITIVE else Case.INSENSITIVE
                handleContains(query, prop, value, case)
            }
        }
        return query
    }

    private fun handleGorEq(query: RealmQuery<T>, prop: String, value: Any) {
        if (value is Date)
            query.greaterThan(prop, value)
    }

    private fun handleLorEq(query: RealmQuery<T>, prop: String, value: Any) {
        // TODO
    }

    private fun handleIn(query: RealmQuery<T>, prop: String, value: Array<*>) {
        when (value[0]) {
            is String -> query.`in`(prop, value as Array<String>)
            is Int -> query.`in`(prop, value as Array<Int>)
        }
    }

    private fun handleNeq(query: RealmQuery<T>, prop: String, value: Any) {
        when (value) {
            is String -> query.notEqualTo(prop, value)
            is Boolean -> query.notEqualTo(prop, value)
            is Int -> query.notEqualTo(prop, value)
        }
    }

    private fun handleEq(query: RealmQuery<T>, prop: String, value: Any) {
        when (value) {
            is String -> query.equalTo(prop, value)
            is Boolean -> query.equalTo(prop, value)
            is Int -> query.equalTo(prop, value)
        }
    }

    private fun handleContains(query: RealmQuery<T>, prop: String, value: Any, case: Case) {
        if (value is String)
            query.contains(prop, value, case)
    }
}
