package com.example.worldcupexplorer.data.remote.interceptor

import com.example.worldcupexplorer.BuildConfig
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Auth-Token", BuildConfig.FOOTBALL_DATA_API_KEY)
            .build()
        return chain.proceed(request)
    }
}
