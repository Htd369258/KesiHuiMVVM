package com.htd.mymvvm.base

/**
 * 请求结果类，保存返回数据
 *
 *
 */

/**
 * 请求结果类，保存返回数据
 *
 *
 */
class DataResult<T>(//请求返回的数据
    var results: T?
) {
    var status //请求状态
            = "ok"
    private var msg //请求消息
            : String? = "Request succeeded"
    var code //类型
            = 0
    var error: String? = null

    var count = 0


    fun getMsg(): String {
        return if (msg == null) "no Msg" else msg!!
    }

    fun setMsg(msg: String?) {
        this.msg = msg
    }

    override fun toString(): String {
        return "DataResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + results +
                '}'
    }

}