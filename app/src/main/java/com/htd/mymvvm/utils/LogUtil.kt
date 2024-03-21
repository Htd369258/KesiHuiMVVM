package com.htd.mymvvm.utils

import android.util.Log

/**
 * LogUtil 是一个日志打印类，用来控制日志的打印，比如正式版本发布的时候取消打印所有日志
 *
 * Created by htd on 16-3-7.
 */
object LogUtil {
    /**
     * 获取是否可打印日志
     *
     * @return
     */
    var isLogable = false
        private set
    private var logLevel = LogPriority.VERBOSE

    /**
     * 同Log.v
     *
     * @param tag
     * @param msg
     * @return
     */
    fun v(tag: String, msg: String): Int {
        return println(LogPriority.VERBOSE, tag, msg)
    }

    /**
     * 同Log.d
     *
     * @param tag
     * @param msg
     * @return
     */
    fun d(tag: String, msg: String): Int {
        return println(LogPriority.DEBUG, tag, msg)
    }

    /**同Log.i
     *
     * @param tag
     * @param msg
     * @return
     */
    fun i(tag: String, msg: String): Int {
        return println(LogPriority.INFO, tag, msg)
    }

    /**
     * 同Log.w
     *
     * @param tag
     * @param msg
     * @return
     */
    fun w(tag: String, msg: String): Int {
        return println(LogPriority.WARN, tag, msg)
    }

    /**
     * 同Log.e
     *
     * @param tag
     * @param msg
     * @return
     */
    fun e(tag: String, msg: String): Int {
        return println(LogPriority.ERROR, tag, msg)
    }

    /**
     * 设置是否打印日志
     *
     * @param logable
     */
    fun setDebugMode(logable: Boolean) {
        isLogable = logable
    }

    /**
     * 设置日志等级
     *
     * @param level
     */
    fun setLogLevel(level: LogPriority) {
        logLevel = level
    }

    /**
     * 日志输出
     *
     * @param priority
     * @param tag
     * @param msg
     * @return
     */
    private fun println(priority: LogPriority, tag: String, msg: String): Int {
        var i = -1
        return if (isLogable && logLevel.ordinal <= priority.ordinal) {
            i = when (priority) {
                LogPriority.VERBOSE -> Log.VERBOSE
                LogPriority.DEBUG -> Log.DEBUG
                LogPriority.INFO -> Log.INFO
                LogPriority.WARN -> Log.WARN
                LogPriority.ERROR -> Log.ERROR
                LogPriority.ASSERT -> Log.ASSERT
            }
            Log.println(i, tag, msg)
        } else {
            -1
        }
    }

    enum class LogPriority {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }
}