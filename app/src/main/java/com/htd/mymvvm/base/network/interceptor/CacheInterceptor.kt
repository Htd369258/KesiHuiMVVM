package com.htd.mymvvm.base.network.interceptor

import android.content.Context
import android.util.Log
import com.htd.mymvvm.utils.AppUtil.Companion.isOnline

import javax.inject.Inject
import kotlin.Throws
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * 缓存拦截器
 *
 *
 * Created by htd on 18-8-1.
 */
class CacheInterceptor @Inject constructor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response: Response = chain.proceed(request)
        response = if (isOnline(context)) {
            val maxAge = 60 // read from cache for 1 minute
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Expires")
                .header("Cache-Control", "public, max-age=")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 3 // tolerate 4-weeks stale
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Expires")
                .header("Cache-Control", "public, only-if-cached, max-stale=")
                .build()
        }
        return response
    }
}