package com.example.mymovieapp.utils

import android.content.Context
import android.content.res.AssetManager
import com.example.mymovieapp.model.auth.CountryPhoneCodeModel
import com.example.mymovieapp.model.auth.CountryPhoneCodesJson
import javax.inject.Inject


class PathHelper(
    val context : Context,
    val jsonConverter: JsonConverter
    ) {


    fun getCountryPhoneCode() : List<CountryPhoneCodeModel>? {
        val parseText = context.assets.readAssetsFile(COUNTRIES_PHONE_CODE_FILE)
        return jsonConverter.convertJsonToObject<CountryPhoneCodesJson>(parseText)?.countries
    }


    private fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName).bufferedReader().use { it.readText() }

    companion object {
        private const val COUNTRIES_PHONE_CODE_FILE = "countryPhoneCodes.json"
    }
}