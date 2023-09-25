package com.example.mymovieapp.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber
import java.util.*

class JsonConverter {

    val instance: Moshi? by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(MoshiArrayListJsonAdapter.FACTORY)
            .build()
    }

    inline fun <reified T> convertJsonToObject(json : String) : T? {
        return try {
            val adapter = instance?.adapter(T::class.java)?.lenient()
            adapter?.fromJson(json)
        }catch (exp : Exception) {
            Timber.tag(TAG).e(exp)
            null
        }
    }

    inline fun <reified T> convertObjectToJson(objectData : T) : String? {
        return try {
            val adapter = instance?.adapter(T::class.java)?.lenient()
            adapter?.toJson(objectData)
        }catch (exp : Exception) {
            Timber.tag(TAG).e(exp)
            null
        }
    }


    companion object {
        val TAG: String = JsonConverter::class.java.simpleName
    }

}