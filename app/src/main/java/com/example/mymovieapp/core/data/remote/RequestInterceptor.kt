package com.example.mymovieapp.core.data.remote

import com.example.mymovieapp.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

internal class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newHttpUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key",API_KEY)
            .build()

        val request = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        return chain.proceed(request)
    }
}