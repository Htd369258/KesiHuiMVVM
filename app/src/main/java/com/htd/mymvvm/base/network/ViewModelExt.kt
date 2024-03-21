package com.htd.mymvvm.base.network


import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.htd.mymvvm.base.ApiException
import com.htd.mymvvm.base.DataResult
import com.htd.mymvvm.base.ui.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * ViewModel扩展方法：启动协程
 * @param block 协程逻辑
 * @param onError 错误回调方法
 * @param onComplete 完成回调方法
 */
fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (e: Throwable) -> Unit = { _: Throwable -> },
    onComplete: () -> Unit = {}
) {
    viewModelScope.launch(
        CoroutineExceptionHandler { _, throwable ->
            run {
                // 这里统一处理错误
                ExceptionUtil.catchException(throwable)
                onError(throwable)
            }
        }
    ) {
        try {
            block.invoke(this)
        } finally {
            onComplete()
        }
    }
}

/**
 * viewModel （协程）请求装换成类似rxjava形式
 */
fun <T> BaseViewModel.reqLaunch(
    api: suspend () -> DataResult<T>,
    result: ApiResultCallBack<T?>,
    showLoading: Boolean = true,//是否显示loading
    showLoadingNow: Boolean = false,//是否立马显示loading
    isShowErrorMsg: Boolean = true,//是否toast错误信息
    isNeedShowErrorView: Boolean = false//是否需要显示加载出错图
) {

    viewModelScope.launch {
        reqApi {
            api.invoke()
        }.loading(
            showLoading,
            showLoadingNow,
            getUIChangeLiveDataEvent().showLoadingNowEvent,
            getUIChangeLiveDataEvent().dismissLoadingDialogEvent
        ).collectData(
            result,
            isShowErrorMsg,
            isNeedShowErrorView,
            getUIChangeLiveDataEvent().showNetworkErrorEvent
        )
    }
}

/**
 * 剥离DataResult
 */
fun <T> Flow<DataResult<T>>.transformData(): Flow<T?> {
    return transform { value ->
        if (value.code == 0) {
            emit(value.results)
        } else {
            throw ApiException(value.getMsg(), value.code)
        }
    }
}

fun <T> reqApi(api: suspend () -> DataResult<T>): Flow<T?> {
    return flow {
        emit(api())
    }.flowOn(Dispatchers.IO).transformData()
}

fun <T> Flow<T>.loading(
    show: Boolean, showLoadingNow: Boolean,
    showLoading: MutableLiveData<Boolean>?,
    dismissLoading: MutableLiveData<Boolean>?
): Flow<T> {
    return this.onStart {
        if (show)
            showLoading?.value = showLoadingNow
    }.onCompletion {
        dismissLoading?.value = true
    }
}

/**
 * 结果回调
 */
suspend fun <T> Flow<T>.collectData(
    result: ApiResultCallBack<T>,
    isShowErrorMsg: Boolean = true,
    isNeedShowErrorView: Boolean,
    showNetworkErrorEvent: MutableLiveData<Boolean>?
) {
    this.catch {
        result.onError(it)
        if (isNeedShowErrorView && it !is ApiException)
            showNetworkErrorEvent?.postValue(true)
        if (isShowErrorMsg)
            ExceptionUtil.catchException(it)
    }.collect {
        try {
            result.onSuccess(it)
        } catch (e: Exception) {
            result.onError(e)
            if (isNeedShowErrorView && e !is ApiException)
                showNetworkErrorEvent?.postValue(true)
            if (isShowErrorMsg)
                ExceptionUtil.catchException(e)
        }
    }
}

/**
 * adapter设置数据
 */
fun <T> BaseQuickAdapter<T, *>.setListData(
    viewModel: BaseViewModel, listData: List<T>
) {
    if (viewModel.isLoadMore()) {
        this.data.addAll(listData)
        this.notifyItemRangeInserted(
            this.data.size - listData.size + this.headerLayoutCount,
            listData.size
        )
        this.notifyDataSetChanged()

    } else {
        if (listData.isEmpty()) {
            this.data.clear()
            this.notifyDataSetChanged()
            viewModel.postShowEmptyViewEvent(true)
        } else {
            this.data.clear()
            this.notifyDataSetChanged()
            this.setList(listData)
        }
    }
}


/**
 * view的显示隐藏
 */
val View.isVisible: Boolean
    get() = visibility == View.VISIBLE
val View.isInVisible: Boolean
    get() = visibility == View.INVISIBLE
val View.isGone: Boolean
    get() = visibility == View.GONE

fun <T : View> View.view(@IdRes res: Int): T {
    return findViewById(res)
}

/**
 * 隐藏键盘
 */
fun View.hideKeyboard(): Boolean {
    clearFocus()
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )
}

/**
 * 显示键盘
 */
fun View.showKeyboard(): Boolean {
    requestFocus()
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        this,
        InputMethodManager.SHOW_IMPLICIT
    )
}

/**
 * view点击,带有防重复点击
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(it!! as T)
    }
}

/**
 * 长按点击
 */
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener {
    block(it as T)
}

//是否可以点击
private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        triggerLastTime = currentClickTime
        flag = true
    }
    return flag
}

//最后点击时间
private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

//点击延迟时间，默认1000ms
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 1000
    set(value) {
        setTag(1123461123, value)
    }

