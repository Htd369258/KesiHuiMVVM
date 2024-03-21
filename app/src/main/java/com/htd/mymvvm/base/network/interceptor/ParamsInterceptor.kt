package com.htd.mymvvm.base.network.interceptor

import android.content.Context
import okhttp3.Interceptor

import okhttp3.Request
import javax.inject.Singleton
import javax.inject.Inject
import kotlin.Throws
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

/**
 * ParamsInterceptor 用来添加通用请求参数的网络拦截器，主要添加 timestamp，sign
 *
 *
 * Created by htd on 18-8-1.
 */
@Singleton
class ParamsInterceptor @Inject constructor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val orgRequest: Request = chain.request()
        val body = orgRequest.body
        val timestamp: String = (System.currentTimeMillis() / 1000).toString()
        return chain.proceed(orgRequest)
    }

    companion object {
        private const val TAG = "请求参数"
    }
}