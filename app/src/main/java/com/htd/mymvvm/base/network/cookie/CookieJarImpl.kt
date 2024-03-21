package com.htd.mymvvm.base.network.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarImpl(cookieStore: CookieStore) : CookieJar {
    val cookieStore: CookieStore

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.add(url, cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url]
    }

    init {
        this.cookieStore = cookieStore
    }
}
