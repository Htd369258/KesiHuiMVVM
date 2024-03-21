package com.htd.mymvvm

import android.app.Application
import com.htd.mymvvm.base.ActivityController
import com.htd.mymvvm.base.CrashHandler
import com.htd.mymvvm.base.dagger.AppInjector

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import java.util.*
import javax.inject.Inject
import com.hjq.toast.ToastUtils

class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var activityController: ActivityController

    @Inject
    lateinit var crashHandler: CrashHandler


    companion object {
        /**
         * 以表名缓存各张表
         */
        lateinit var myApp: App
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
        ToastUtils.init(this)
        AppInjector.init(this)
        crashHandler.init()
        setRxJavaErrorHandler()
    }

    private fun setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler { throwable: Throwable -> throwable.printStackTrace() }
    }

    override fun androidInjector(): AndroidInjector<Any>? = dispatchingAndroidInjector
}
