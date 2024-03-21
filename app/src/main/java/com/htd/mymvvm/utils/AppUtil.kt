package com.htd.mymvvm.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Process
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.hjq.toast.ToastUtils


/**
 * AppUtils是一个android工具类，主要包含一些常用的有关android调用的功能，比如拨打电话，判断网络，获取屏幕宽高等等
 */
class AppUtil {

    companion object {

        /**
         * 获得当前进程的名字
         *
         * @param context
         * @return 进程号
         */
        private fun getCurProcessName(context: Context): String? {
            val pid = Process.myPid()
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in activityManager
                .runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
            return null
        }

        /**
         * 判断是否是当前同一线程
         *
         * @param context
         * @return
         */
        fun isCurProcess(context: Context): Boolean {
            val processName = getCurProcessName(context)
            if (!TextUtils.isEmpty(processName)) {
                if (processName == context.packageName) {
                    return true
                }
            } else {
                return false
            }
            return false
        }

        fun copy(context: Context, content: String?) {
            val cm =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", content)
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
            ToastUtils.show("复制成功")
        }


        /**
         * 判断当前是否有网络连接
         *
         * @param context 当前上下文对象
         * @return boolean
         */
        fun isOnline(context: Context): Boolean {

            val manager = context
                .getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo

            return info != null && info.isConnected
        }


        /**
         * 获取当前程序的版本
         *
         * @param context 当前上下文对象
         * @return String 当前版本号
         */
        fun getAppVersionName(context: Context): String {
            var version = "1.0.0"
            try {
                version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return version
        }

        fun getChannel(context: Context): String {
            try {
                val appInfo = context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
                return appInfo.metaData.getString("UMENG_CHANNEL") ?: return "gw-ymgy"
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return "gw-ymgy"
        }

        /**
         * 是否小米渠道
         */
        fun isXMChannel(context: Context): Boolean {
            return getChannel(context) == "xm-ymgy"
        }

        /**
         * 获取通知栏权限是否开启
         */
        fun isNotificationEnabled(context: Context): Boolean {
            val notificationManagerCompat: NotificationManagerCompat =
                NotificationManagerCompat.from(context)
            return notificationManagerCompat.areNotificationsEnabled()
            val mAppOps =
                context.getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
            val appInfo: ApplicationInfo = context.applicationInfo
            val pkg = context.packageName
            val uid: Int = appInfo.uid
            var appOpsClass: Class<*>? = null

            try {
                appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass.getMethod(
                    "checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                    String::class.java
                )
                val opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION")
                val value = opPostNotificationValue[Int::class.java] as Int
                return checkOpNoThrowMethod.invoke(
                    mAppOps,
                    value,
                    uid,
                    pkg
                ) as Int === AppOpsManager.MODE_ALLOWED
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

    }
}