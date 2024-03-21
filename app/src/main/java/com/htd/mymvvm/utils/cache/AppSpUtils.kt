package com.htd.mymvvm.utils.cache

/**
 * Describe : app数据保存
 */
object AppSpUtils {
    /**
     * 表名
     */
    private const val TABLE_NAME = "table_app"
    const val KEY_TRADE_EXPLAIN_DATE = "key_trade_explain_date"
    const val KEY_ENVIRONMENT_INDEX = "key_environment_index"

    /**
     * 获取spUtils实例
     *
     * @return
     */
    val sPUtils: SPUtils
        get() = SPUtils.getInstance(TABLE_NAME)
}