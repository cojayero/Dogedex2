package com.cojayero.dogedex2.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
private val TAG = "ApiServiceInterceptor"
object ApiServiceInterceptor:Interceptor {
    const val  NEEDS_AUTH_HEADER_KEY = "needs_authentication"
    private var sessionToken: String? = null
    fun setSessionToken(sessionToken:String){
        Log.d(TAG, "setSessionToken: $sessionToken")
        this.sessionToken = sessionToken
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        if (request.header(NEEDS_AUTH_HEADER_KEY) != null){
            // needs credentials
            Log.d(TAG, "intercept: needs credentials")
            if(sessionToken == null){
                Log.d(TAG, "intercept: sessionToken is null")
                throw RuntimeException("Need to be authenticated")
            } else {
                Log.d(TAG, "intercept: Auth-token : $sessionToken")
                requestBuilder.addHeader("AUTH-TOKEN",sessionToken!!)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}