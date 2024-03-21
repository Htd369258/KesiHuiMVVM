package com.htd.mymvvm.base.network.cookie

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStore {
    /**  添加cookie  */
    fun add(httpUrl: HttpUrl, cookie: Cookie)

    /** 添加指定httpurl cookie集合  */
    fun add(httpUrl: HttpUrl, cookies: List<Cookie>)

    /** 根据HttpUrl从缓存中读取cookie集合  */
    operator fun get(httpUrl: HttpUrl): List<Cookie>

    /** 获取全部缓存cookie  */
    fun getAllCookies(): List<Cookie>

    /**  移除指定httpurl cookie集合  */
    fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean

    /** 移除所有cookie  */
    fun removeAll(): Boolean
}