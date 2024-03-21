package com.htd.mymvvm.base.network


import android.accounts.NetworkErrorException
import android.content.Intent
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.hjq.toast.ToastUtils

import com.htd.mymvvm.App
import com.htd.mymvvm.base.ApiException

import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 异常工具类
 * 协程请求方式用到
 */
object ExceptionUtil {

    /**
     * 处理异常，toast提示错误信息
     */
    fun catchException(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                catchHttpException(e.code())
            }

            is SocketTimeoutException -> {
                showToast("error_net_time_out")
            }

            is UnknownHostException, is NetworkErrorException -> {
                showToast("error_net")
            }

            is MalformedJsonException, is JsonSyntaxException -> {
                showToast("error_server_json")
            }

            is InterruptedIOException -> {
                showToast("服务器连接失败，请稍后重试")
            }
            // 自定义接口异常
            is ApiException -> {
                showToast(e.msg, e.code)
                if (e.code == 201) {

                    try {
                        // App.myApp.startActivity(
                        //   Intent(App.myApp, LoginActivity::class.java).setFlags(
                        //     Intent.FLAG_ACTIVITY_NEW_TASK
                        //)
                        // )
                    } catch (e: Exception) {
                    }
                }
            }

            is ConnectException -> {
                showToast("连接服务器失败")
            }

            else -> {
                showToast("error_do_something_fail")
            }
        }
    }

    /**
     * 处理网络异常
     */
    private fun catchHttpException(errorCode: Int) {
        if (errorCode in 200 until 300) return// 成功code则不处理
        showToast(
            catchHttpExceptionCode(
                errorCode
            ), errorCode
        )
    }


    /**
     * toast提示
     */
    private fun showToast(errorMsg: String, errorCode: Int = -1) {
        ToastUtils.show(errorMsg)
    }

    /**
     * 处理网络异常
     */
    private fun catchHttpExceptionCode(errorCode: Int): String = when (errorCode) {
        in 500..600 -> "error_server"
        in 400 until 500 -> "error_request"
        else -> "error_request"
    }
}