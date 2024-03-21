package com.htd.mymvvm.base

import retrofit2.Retrofit

/**
 * BaseRepository app的数据仓库类，所有需要的数据都通过它获得
 *
 *
 */
abstract class BaseRepository<T>(retrofit: Retrofit) {

    var apiStore: T

    init {
        apiStore = retrofit.create(providerRepositoryType())
    }


    /**
     * 定义接口类型
     *
     * @return
     */
    protected abstract fun providerRepositoryType(): Class<T>

    companion object {
        protected const val TAG = "BaseRepository"

        //对于列表,每页显示的内容条数
        const val pageSum = 10
    }

}