package com.example.myapplication.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    companion object {
        private const val AUTH_TOKEN = "317da3a9-98d5-4bfb-a45a-19cc27b67824"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header(
            "Authorization",
            AUTH_TOKEN
        )
        val request = builder.build()
        return chain.proceed(request)
    }
}