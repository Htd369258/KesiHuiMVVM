package com.htd.mymvvm.base.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.htd.mymvvm.base.ApiException
import com.htd.mymvvm.base.binding.BindingAction
import com.htd.mymvvm.base.binding.BindingCommand
import com.htd.mymvvm.utils.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel(application: Application) : AndroidViewModel(
    application
) {

    protected var TAG = javaClass.simpleName

    val onRefreshCommand: BindingCommand<Boolean> by lazy {
        BindingCommand(object : BindingAction {
            override fun call() {
                onRefresh()
            }
        })
    }

    val onLoadMoreCommand: BindingCommand<Boolean> by lazy {
        BindingCommand(object : BindingAction {
            override fun call() {
                onLoadMore()
            }
        })
    }

    open fun onRefresh() {}
    open fun onLoadMore() {}
    private val uiChangeLiveData by lazy { UIChangeLiveData() }

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun addTask(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun removeTask(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }

    override fun onCleared() {
        clearTask()
        LogUtil.i(TAG, "onCleared: ----------")
        super.onCleared()
    }

    open fun clearTask() {
        compositeDisposable.clear()
    }

    private var page = -1

    open var isNeedShowErrorView = false

    fun getUIChangeLiveDataEvent() = uiChangeLiveData

    fun loadComplete() {
        postDismissLoadingDialogEvent(true)
    }

    fun loadError(e: Throwable) {
        var errorMsg = ""
        if (e is ApiException) {
            errorMsg = e.msg
        } else {
            errorMsg = "网络异常"
        }
        LogUtil.i(TAG, "loadError: ---------")
        if (e !is ApiException && isNeedShowErrorView) {
            postShowNetworkErrorEvent(true)
        }
        postShowToastMessageEvent(errorMsg)
        postDismissLoadingDialogEvent(true)

    }

    open fun loadListData(refresh: Boolean, isShowLoadingNow: Boolean = false) {

        isNeedShowErrorView = if (refresh) {
            refreshPageData()
            true
        } else {
            false
        }
    }

    open fun loadMore(): Int {
        page++
        return page
    }

    /**
     * 刷新页面数据
     */
    open fun refreshPageData() {
        page = -1
    }

    open fun isLoadMore(): Boolean {
        return page > 0
    }


    class UIChangeLiveData {
        val showLoadingNowEvent: MutableLiveData<Boolean> = MutableLiveData()

        val dismissLoadingDialogEvent: MutableLiveData<Boolean> = MutableLiveData()

        val showToastMessageEvent: MutableLiveData<String> = MutableLiveData()

        val showEmptyViewEvent: MutableLiveData<Boolean> = MutableLiveData()

        val showNetworkErrorEvent: MutableLiveData<Boolean> = MutableLiveData()

    }


    open fun postShowLoadingNowEvent(isNow: Boolean) {
        uiChangeLiveData.showLoadingNowEvent.postValue(isNow)
    }

    open fun postDismissLoadingDialogEvent(dismiss: Boolean) {
        uiChangeLiveData.dismissLoadingDialogEvent.postValue(dismiss)
    }

    open fun postShowEmptyViewEvent(show: Boolean) {
        uiChangeLiveData.showEmptyViewEvent.postValue(show)
    }

    open fun postShowToastMessageEvent(message: String) {
        uiChangeLiveData.showToastMessageEvent.postValue(message)
    }

    open fun postShowNetworkErrorEvent(show: Boolean) {
        uiChangeLiveData.showNetworkErrorEvent.postValue(show)
    }


}