package com.htd.mymvvm.utils

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import android.os.Build


/**
 *
 * 描述:获取当前设备的屏幕参数
 */
object ScreenUtil {
    /**
     * 获取屏幕的宽高 单位px
     * x = width
     * y = height
     * @param context Context
     * @return Point
     */
    fun getScreenPoint(context: Context): Point {
        val screenPoint = Point()
        val displayMetrics = context.resources.displayMetrics
        screenPoint.x = displayMetrics.widthPixels
        screenPoint.y = displayMetrics.heightPixels
        return screenPoint
    }

    /**
     * 获取屏幕宽
     *
     * @param context 上下文
     * @return int ，单位px
     */
    fun getWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高
     *
     * @param context 上下文
     * @return int ，单位px
     */
    fun getHeight(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.heightPixels
    }

    /**
     * 获取屏幕的真实高度，包含导航栏、状态栏
     * @param context 上下文
     * @return int,单位px
     */
    fun getRealHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm)
        } else {
            display.getMetrics(dm)
        }
        return dm.heightPixels
    }

    /**
     * 获取屏幕的真实高度，包含导航栏、状态栏
     * @param context 上下文
     * @return int,单位px
     */
    fun getRealWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm)
        } else {
            display.getMetrics(dm)
        }
        return dm.widthPixels
    }

    /**
     * 是否在屏幕右侧
     *
     * @param mContext 上下文
     * @param xPos     位置的x坐标值
     * @return true：是。
     */
    fun isInRight(mContext: Context, xPos: Int): Boolean {
        return xPos > getWidth(mContext) / 2
    }

    /**
     * 是否在屏幕左侧
     *
     * @param mContext 上下文
     * @param xPos     位置的x坐标值
     * @return true：是。
     */
    fun isInLeft(mContext: Context, xPos: Int): Boolean {
        return xPos < getWidth(mContext) / 2
    }

    /**
     * 获取虚拟导航栏高度
     * @param activity 上下文
     * @return int,单位px
     */
    fun getNavigationHeight(activity: Context?): Int {
        if (activity == null) {
            return 0
        }
        val resources = activity.resources
        val resourceId = resources.getIdentifier(
            "navigation_bar_height",
            "dimen", "android"
        )
        var height = 0
        if (resourceId > 0) {
            //获取NavigationBar的高度
            height = resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕像素密度相对于标准屏幕(160dpi)倍数
     *
     * @param context 当前上下文对象
     * @return float 屏幕像素密度
     */
    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 将dp转换成px
     *
     * @param context 当前上下文对象
     * @param dp      dp值
     * @return px的值
     */
    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int {
        return (getScreenDensity(context) * dp + 0.5f).toInt()
    }

    /**
     * 将dp转换成px
     *
     * @param context 当前上下文对象
     * @param dp      dp值
     * @return px的值
     */
    fun dp2Px(context: Context, dp: Float): Float {
        return getScreenDensity(context) * dp + 0.5f
    }

    /**
     * 将px转换成dp
     *
     * @param context 当前上下文对象
     * @param px      px的值
     * @return dp值
     */
    fun px2dp(context: Context, px: Float): Int {
        return if (px == 0f) {
            0
        } else {
            (px / getScreenDensity(context) + 0.5f).toInt()
        }
    }

    fun sp2px(context: Context, sp: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }
}