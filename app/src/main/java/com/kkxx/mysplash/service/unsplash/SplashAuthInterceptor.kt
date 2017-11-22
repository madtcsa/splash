package com.kkxx.mysplash.service.unsplash

import com.kkxx.mysplash.Splash
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Auth interceptor.
 *
 * A interceptor for {@link retrofit2.Retrofit}, it can add authorization information into the
 * HTTP request header.
 *
 * @author chenwei
 * 2017/11/5
 */
class SplashAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request: Request = chain!!.request().newBuilder()
                .addHeader("Authorization", "Client-ID " +
                        Splash.getSplashAppId(false))
                .build()
        return chain.proceed(request)
    }
}