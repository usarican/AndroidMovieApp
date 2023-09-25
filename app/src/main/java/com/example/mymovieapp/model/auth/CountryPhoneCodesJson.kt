package com.example.mymovieapp.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryPhoneCodesJson(
    @Json(name = "success") val success : Boolean? = false,
    @Json(name = "countries") val countries : List<CountryPhoneCodeModel>
)
@JsonClass(generateAdapter = true)
data class CountryPhoneCodeModel(
    @Json(name = "name") val countryName: String,
    @Json(name = "flag") val countryFlag: String,
    @Json(name = "dial_code") val countryPhoneCode: String,
    @Json(name = "code") val countryCode: String
)