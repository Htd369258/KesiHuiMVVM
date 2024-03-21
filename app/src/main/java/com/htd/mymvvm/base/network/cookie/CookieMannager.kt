package com.htd.mymvvm.base.network.cookie

import android.content.Context

object CookieMannager {
    private var persistentCookieStore: PersistentCookieStore? = null
    fun getInstance(context: Context): PersistentCookieStore {
        if (persistentCookieStore == null) {
            synchronized(PersistentCookieStore::class.java) {
                if (persistentCookieStore == null) {
                    persistentCookieStore = PersistentCookieStore(context)
                }
            }
        }
        return persistentCookieStore!!
    }
}
