package com.htd.mymvvm.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.app.ActivityCompat
import com.htd.mymvvm.utils.DateUtil.currentTime
import com.htd.mymvvm.utils.LogUtil
import com.htd.mymvvm.utils.file.SDCardUtil

import com.hjq.toast.ToastUtils
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

import java.io.File


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 * Created by htd on 16-8-1.
 */
@Singleton
class CrashHandler @Inject constructor(
    // 程序的Context对象
    private val context: Context,
    //App的Activity管理类
    private val activityController: ActivityController
) : Thread.UncaughtExceptionHandler {
    private val TAG = "CrashHandler1"

    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    // 用来存储设备信息和异常信息
    private var infos: MutableMap<String, String>? = null

    //保存崩溃信息的目录
    private var path: String? = null

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            ToastUtils.show("很抱歉,我要崩溃了")
        }
    }

    /**
     * 初始化CrashHandler
     */
    fun init() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        infos = HashMap()
        val externalSaveDir = context.externalCacheDir
        val parent = externalSaveDir ?: context.cacheDir
        path = parent?.absolutePath.toString() + "/app"
        var f = File(path, "app")
        if (!f.exists()) {
            f.mkdir()
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        handler.sendEmptyMessage(0)
        LogUtil.d(TAG, "uncaughtException: -----" + ex.message.toString())
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        }
        activityController.exitApplication(false)
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }

        // 使用Toast来显示异常信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                collectDeviceInfo()
                saveCrashInfo2File(ex)
                Looper.loop()
            }
        }.start()
        return true
    }

    /**
     * 收集设备参数信息
     */
    fun collectDeviceInfo() {
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos!![field.name] = field[null].toString()
                LogUtil.d(TAG, field.name + " : " + field[null])
            } catch (e: Exception) {
                LogUtil.e(TAG, "an error occured when collect crash info")
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in infos!!) {
            sb.append("=\n")
        }
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)

        //打印日志，以便调试
        LogUtil.e(TAG, sb.toString())
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //保存错误信息到文件
            try {
                val timestamp = System.currentTimeMillis()
                val time = currentTime()
                val fileName = "crash-$timestamp.log"
                path?.let {
                    SDCardUtil.saveFileToSDCard(it, fileName, sb.toString())
                }
                return fileName
            } catch (e: Exception) {
                LogUtil.e(
                    TAG,
                    "an error occured while writing file...\npath"
                )
                e.printStackTrace()
            }
        } else {
            LogUtil.d(TAG, "没有写入sd卡的权限")
        }
        return null
    }

}