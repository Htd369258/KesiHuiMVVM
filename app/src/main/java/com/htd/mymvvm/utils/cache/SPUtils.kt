package com.htd.mymvvm.utils.cache

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.collection.SimpleArrayMap
import com.google.gson.Gson
import com.htd.mymvvm.App


/**
 * Describe : shared preference 的工具类
 */
class SPUtils private constructor(spName: String) {
    private val sp: SharedPreferences = App.myApp
        .getSharedPreferences(spName, Context.MODE_PRIVATE)

    /**
     * 保存String数据
     *
     * @param key
     * @param value
     * @param isCommit 是否提交
     */
    fun put(key: String, value: String?, isCommit: Boolean): SPUtils {
        if (isCommit) {
            sp.edit().putString(key, value).commit()
        } else {
            sp.edit().putString(key, value).apply()
        }
        return this
    }

    /**
     * 保存String数据
     *
     * @param key
     * @param value
     */
    fun put(key: String, value: String?): SPUtils {
        return put(key, value, true) //默认提交
    }

    /**
     * 保存int数据
     *
     * @param key
     * @param value
     * @param isCommit 是否提交
     */
    fun put(key: String, value: Int, isCommit: Boolean): SPUtils {
        if (isCommit) {
            sp.edit().putInt(key, value).commit()
        } else {
            sp.edit().putInt(key, value).apply()
        }
        return this
    }

    /**
     * 保存int数据
     *
     * @param key
     * @param value
     */
    fun put(key: String, value: Long): SPUtils {
        return put(key, value, true)
    }

    /**
     * 保存long数据
     *
     * @param key
     * @param value
     * @param isCommit 是否提交
     */
    fun put(key: String, value: Long, isCommit: Boolean): SPUtils {
        if (isCommit) {
            sp.edit().putLong(key, value).commit()
        } else {
            sp.edit().putLong(key, value).apply()
        }
        return this
    }

    /**
     * 保存long数据
     *
     * @param key
     * @param value
     */
    fun put(key: String, value: Int): SPUtils {
        return put(key, value, true)
    }

    /**
     * 保存boolean数据
     *
     * @param key
     * @param value
     * @param isCommit 是否提交
     */
    fun put(key: String, value: Boolean, isCommit: Boolean): SPUtils {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit()
        } else {
            sp.edit().putBoolean(key, value).apply()
        }
        return this
    }

    /**
     * 保存boolean数据
     *
     * @param key
     * @param value
     */
    fun put(key: String, value: Boolean): SPUtils {
        return put(key, value, true)
    }

    /**
     * 获取字符类型的数据
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    fun getString(key: String, defaultValue: String?): String? {
        return sp.getString(key, defaultValue)
    }

    /**
     * 获取字符类型的数据
     *
     * @param key
     * @return
     */
    fun getString(key: String): String? {
        return sp.getString(key, "") //默认值为空
    }

    /**
     * 获取Long的数据
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    fun getLong(key: String, defaultValue: Long): Long {
        return sp.getLong(key, defaultValue)
    }

    /**
     * 获取Long的数据
     *
     * @param key
     * @return
     */
    fun getLong(key: String): Long {
        return sp.getLong(key, 0) //默认值为0
    }

    /**
     * 获取整型的数据
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    /**
     * 获取整型的数据
     *
     * @param key
     * @return
     */
    fun getInt(key: String): Int {
        return sp.getInt(key, 0) //默认值为0
    }

    /**
     * 获取布尔类型的数据
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    /**
     * 获取布尔类型的数据
     *
     * @param key
     * @return
     */
    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false) //默认值为false
    }

    /**
     * 提交对象
     * @param key
     * @param object
     */
    fun put(key: String, `object`: Any?): SPUtils {
        val gson = Gson()
        val json = gson.toJson(`object`)
        return put(key, json)
    }

    /**
     * 获取对象
     * @param key
     * @param clazz
     * @return
     */
    operator fun <T> get(key: String, clazz: Class<T>?): T? {
        val json = getString(key)
        return if (TextUtils.isEmpty(json)) {
            null
        } else try {
            val gson = Gson()
            gson.fromJson(json, clazz)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        /**
         * 以表名缓存各张表
         */
        private val SP_UTILS_MAP =
            SimpleArrayMap<String, SPUtils>()

        fun getInstance(spName: String): SPUtils {
            var spUtils = SP_UTILS_MAP[spName]
            if (spUtils == null) {
                synchronized(SPUtils::class.java) {
                    if (spUtils == null) {
                        spUtils = SPUtils(spName)
                        SP_UTILS_MAP.put(spName, spUtils)
                    }
                }
            }
            return spUtils!!
        }
    }

}