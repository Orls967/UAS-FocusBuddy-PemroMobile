package com.example.focusbuddyapp.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val getAuthToken: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = getAuthToken()
        
        return if (!token.isNullOrBlank()) {
            val builder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            chain.proceed(builder.build())
        } else {
            chain.proceed(original)
        }
    }
}
