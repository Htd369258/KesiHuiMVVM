package com.htd.mymvvm.base.dagger

import android.app.Application
import com.htd.mymvvm.BuildConfig

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.htd.mymvvm.base.network.cookie.CookieJarImpl
import com.htd.mymvvm.base.network.cookie.CookieMannager
import com.htd.mymvvm.base.network.interceptor.BaseUrlInterceptor
import com.htd.mymvvm.base.network.interceptor.CacheInterceptor
import com.htd.mymvvm.base.network.interceptor.ParamsInterceptor
import com.htd.mymvvm.constants.EnvironmentConstants.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * NetworkModule 网络请求模块，提供OkHttpClient，Retrofit
 *
 *
 * Created by htd on 18-8-1.
 */
@Module
class NetworkModule {
    /**
     * 提供 OkHttpClient
     * @param paramsInterceptor
     * @param cache
     * @return
     */
    @Singleton
    @Provides
    fun privodeOkHttpClient(
        paramsInterceptor: ParamsInterceptor,
        cache: Cache,
        context: Application,
        cacheInterceptor: CacheInterceptor
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cookieJar(CookieJarImpl(CookieMannager.getInstance(context)))
            .cache(cache) //                .sslSocketFactory(SSLFactoryUtil.getSocketFactory(context), SSLFactoryUtil.getTrustManager())
            .hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
            .addInterceptor(BaseUrlInterceptor())
            .addInterceptor(paramsInterceptor)
            .addInterceptor(cacheInterceptor)//添加 paramsInterceptor
        if (BuildConfig.DEBUG) {
            //打印请求相关的信息
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    /**
     * 提供 Retrofit
     * @param okHttpClient
     * @return
     */
    @Singleton
    @Provides
    fun privodeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
            .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
            .client(okHttpClient)
            .build()
    }

    /**
     * 生成缓存
     *
     * @return
     */
    @Singleton
    @Provides
    fun privodeCache(context: Application): Cache {
        val cacheFile = File(context.cacheDir, "NetData")
        return Cache(cacheFile, 10 * 1024 * 1024)
    }
}