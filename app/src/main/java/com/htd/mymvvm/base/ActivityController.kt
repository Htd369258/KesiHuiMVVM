package com.htd.mymvvm.base

import android.os.Process
import com.htd.mymvvm.base.ui.BaseActivity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.exitProcess

/**
 * ActivityManager app的activity的管理类
 *
 *
 * Created by htd on 18-8-1.
 */
@Singleton
class ActivityController @Inject constructor() {
    //Activity 容器
    private val activities: MutableList<BaseActivity<*, *>>

    /**
     * 将activity加入列表
     *
     * @param activity
     */
    fun addActivity(activity: BaseActivity<*, *>?) {
        if (activity != null) {
            activities.add(activity)
        }
    }

    /**
     * 将activity移除列表
     *
     * @param activity
     */
    fun removeActivity(activity: BaseActivity<*, *>?) {
        if (activity != null) {
            activities.remove(activity)
        }
    }

    /**
     * 退出程序
     */
    fun exitApplication(isNormarExit: Boolean) {
        for (activity in activities) {
            activity.finish()
        }

        //退出，0是正常退出，1是异常退出
        if (isNormarExit) {
            exitProcess(0)
        } else {
            exitProcess(1)
        }

        // 杀死进程
        Process.killProcess(Process.myPid())
    }

    init {
        activities = ArrayList()
    }
}