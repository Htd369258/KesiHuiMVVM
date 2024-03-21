package com.htd.mymvvm.constants

import com.htd.mymvvm.BuildConfig
import com.htd.mymvvm.utils.cache.AppSpUtils
import com.htd.mymvvm.utils.cache.AppSpUtils.sPUtils

/**
 * Describe : 与环境有关
 */
object EnvironmentConstants {
    val ENVIRONMENT_NAMES = arrayOf(
        "正式环境", "测试环境"
    )
    const val TEST_BASE_URL = "https://itunes.apple.com/"
    const val ONLINE_BASE_URL = "https://itunes.apple.com/"

    var BASE_URL = ONLINE_BASE_URL

    /**
     * 切换环境
     *
     * @param index
     */
    fun switchEnvironment(index: Int) {
        if (index == 0) { //正式环境
            BASE_URL = ONLINE_BASE_URL
        } else if (index == 1) { //测试环境
            BASE_URL = TEST_BASE_URL

        }
        sPUtils.put(AppSpUtils.KEY_ENVIRONMENT_INDEX, index)
    }

    /**
     * 切换到到当前环境
     */
    fun switchToCurrentEnvironment() {
        if (isDebug) {
            val index = sPUtils.getInt(AppSpUtils.KEY_ENVIRONMENT_INDEX, 0)
            switchEnvironment(index)
        }
    }

    /**
     * 是否是debug环境
     *
     * @return
     */
    val isDebug: Boolean
        get() = BuildConfig.DEBUG

    fun isBaseUrl(url: String?): Boolean {
        if (url != null) {
            if (url.contains(TEST_BASE_URL) || url.contains(
                    ONLINE_BASE_URL
                )
            ) {
                return true
            }
        }
        return false
    }
}