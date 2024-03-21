package com.htd.mymvvm.base

data class ApiException(var msg: String, var code: Int) : RuntimeException()