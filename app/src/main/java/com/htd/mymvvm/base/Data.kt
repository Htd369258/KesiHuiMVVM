package com.htd.mymvvm.base

import android.text.TextUtils

/**
 * 数据的基类
 *
 *
 * Created by htd on 18-8-1.
 */
class Data {
    //    public String imageBase = BuildConfig.ManageBaseImgUrl;
    var msg: String? = "no Msg"

    var isSuccess = false

    var code = 0

    var err: String? = null

    var isEncode = false

    var status: String? = null

    var ids: String? = null
        get() = if (field == null) "" else field

    /**
     * 过滤null
     *
     * @param str
     * @return
     */
    fun notNull(str: String): String {
        return if (TextUtils.isEmpty(str) || str.contains("null")) {
            ""
        } else str
    }

    var message: String?
        get() = if (msg == null) err else msg
        set(message) {
            msg = message
        }

    override fun toString(): String {
        return "Data{" +
                "message='" + msg + '\'' +
                ", success=" + isSuccess +
                ", code=" + code +
                '}'
    }
}