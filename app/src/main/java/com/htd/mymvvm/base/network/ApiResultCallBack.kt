package com.htd.mymvvm.base.network

abstract class ApiResultCallBack<T> {

    abstract fun onSuccess(t: T)

    open fun onError(e: Throwable) {}
}