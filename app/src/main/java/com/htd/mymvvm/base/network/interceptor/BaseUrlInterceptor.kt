package com.htd.mymvvm.base.network.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取request

        // 获取request
        val request = chain.request()
        // 从request中获取原有的HttpUrl实例oldHttpUrl
        // 从request中获取原有的HttpUrl实例oldHttpUrl
        val oldHttpUrl = request.url
        //        if (EnvironmentConstants.isDebug() && EnvironmentConstants.isBaseUrl(oldHttpUrl.toString())) {
//            // 获取request的创建者builder
//            Request.Builder builder = request.newBuilder();
//            HttpUrl newBaseUrl = HttpUrl.parse(EnvironmentConstants.BASE_URL);
//            // 重建新的HttpUrl，修改需要修改的url部分
//            HttpUrl newFullUrl = oldHttpUrl
//                    .newBuilder()
//                    // 更换网络协议
//                    .scheme(newBaseUrl.scheme())
//                    // 更换主机名
//                    .host(newBaseUrl.host())
//                    // 更换端口
//                    .port(newBaseUrl.port())
//                    .build();
//            // 重建这个request，通过builder.url(newFullUrl).build()；
//            // 然后返回一个response至此结束修改
//            return chain.proceed(builder.url(newFullUrl).build());
//        }
        return chain.proceed(request)
    }
}