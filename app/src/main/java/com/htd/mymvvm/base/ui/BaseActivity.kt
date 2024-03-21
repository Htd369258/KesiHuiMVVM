package com.htd.mymvvm.base.ui

import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils

import com.htd.mymvvm.App
import com.htd.mymvvm.R
import com.htd.mymvvm.base.ActivityController
import com.htd.mymvvm.base.LoadingDialog
import com.htd.mymvvm.constants.EnvironmentConstants.switchToCurrentEnvironment
import com.htd.mymvvm.utils.AppUtil
import com.htd.mymvvm.utils.LogUtil
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    HasAndroidInjector, DialogInterface.OnCancelListener {

    protected var TAG = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    abstract fun onBindViewModel(): Class<VM>

    open val viewModel: VM by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory).get(onBindViewModel())
    }

    val activityController: ActivityController by lazy {
        var myApp = application as App
        myApp.activityController
    }

    private lateinit var dialog: LoadingDialog

    private var isComplete = true

    private lateinit var handler: Handler

    private lateinit var cancleRunnable: Runnable

    lateinit var viewDataBinding: V

    lateinit var myEmptyView: View

    lateinit var myErrorView: View


    open val inflater by lazy {
        LayoutInflater.from(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) { //内存重启时切换到当前环境
            switchToCurrentEnvironment()
        }
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
        initLoadingDialog()
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutResID())
        viewDataBinding.setVariable(initViewModelVariableId(), viewModel)
        viewDataBinding.lifecycleOwner = this
        activityController.addActivity(this)
        handler = Handler()
        cancleRunnable = Runnable {
            if (!isComplete && dialog != null && dialog!!.isShowing) {
                dialog?.cancel()
                dismissLoadingDialog()
                showToast("网络异常，请检查网络")
            }
        }
        initUiChangeEvent()
        initViewObservable()
        initView(savedInstanceState)
        initClickListener()
    }


    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initViewModelVariableId(): Int

    open fun initViewObservable() {}

    open fun initClickListener() {}

    override fun onDestroy() {
        activityController.removeActivity(this)
        dismissLoadingDialog()
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        viewDataBinding.unbind()

        super.onDestroy()
    }

    override fun onCancel(p0: DialogInterface?) {
        dismissLoadingDialog()
        dismissFragmentLoadingDialog()
    }

    /**
     * 初始化UI 相当onCreate方法
     *
     * @param savedInstanceState
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 获取布局
     *
     * @return
     */
    @LayoutRes
    protected abstract fun getLayoutResID(): Int

    /**
     * 注册eventbus
     */
    open fun isRegisterEventBus(): Boolean {
        return false
    }

    /**
     * 设置应用的字体不随系统设置的更改而更改
     */
    override fun getResources(): Resources? {
        val resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        resources.updateConfiguration(config, resources.displayMetrics)
        return resources
    }


    private fun initUiChangeEvent() {

        viewModel.getUIChangeLiveDataEvent().showLoadingNowEvent.observe(
            this
        ) {
            if (it) {
                showLoadingDialogNow(true)
            } else {
                showLoadingDialog()
            }
        }
        viewModel.getUIChangeLiveDataEvent().dismissLoadingDialogEvent.observe(
            this
        ) {
            dismissLoadingDialog()
        }
        viewModel.getUIChangeLiveDataEvent().showToastMessageEvent.observe(
            this
        ) {
            showToast(it)
        }
        viewModel.getUIChangeLiveDataEvent().showEmptyViewEvent.observe(
            this
        ) {
            if (it) showEmptyView()
        }
        viewModel.getUIChangeLiveDataEvent().showNetworkErrorEvent.observe(
            this
        ) {
            if (it) showErrorView()
        }
    }


    //********************************************************网络请求相关方法：显示loading，空布局，网络错误布局，toast等等*******************************************
    /**
     * 初始化loadingDialog
     */
    protected open fun initLoadingDialog() {
        dialog = LoadingDialog(this)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
        dialog?.setOnCancelListener(this)
//        dialog?.window?.setDimAmount(0.0f)
    }

    /**
     * 显示加载对话框
     */
    open fun showLoadingDialog() {
        isComplete = false
        //延迟0.16秒显示加载对话框,如果网速较快,在0.16秒内返回了数据,则不显示加载对话框
        if (handler != null) {
            handler?.postDelayed({
                if (!isComplete) {
                    showLoadingDialogNow(true)
                }
            }, 800)
        }
    }

    /**
     * 立即显示加载对话框
     */
    open fun showLoadingDialogNow(timeout: Boolean) {
        isComplete = false
        if (AppUtil.isOnline(applicationContext)) {
            if (dialog != null && !dialog?.isShowing!!) {
                LogUtil.i("showLoadingDialog", "显示加载对话框")
                if (this == null || isFinishing) {
                    return
                }
                dialog?.show()
                if (timeout) {
                    cancleRunnable?.let { handler?.postDelayed(it, 15000) }
                }
            }
        }
    }

    /**
     * 取消加载对话框
     */
    open fun dismissLoadingDialog() {
        isComplete = true
        cancleRunnable?.let { handler?.removeCallbacks(it) }
        if (dialog != null && dialog!!.isShowing) {
            LogUtil.i("showLoadingDialog", "取消加载对话框")
            dialog?.dismiss()
        }
        providerRefreshLayout()?.finishRefresh()
        providerRefreshLayout()?.finishLoadMore()
    }

    private fun dismissFragmentLoadingDialog() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            supportFragmentManager.fragments.forEach {
                if (it is BaseFragment<*, *>) {
                    it.dismissRefreshLayout()
                }
            }
        }
    }

    open fun timeOut() {}

    /**
     * 获取列表的空布局
     *
     * @return
     */
    protected open fun getEmptyView(): View? {
        if (!::myEmptyView.isInitialized) myEmptyView =
            inflater.inflate(R.layout.empty_layout, null, false)
        return myEmptyView
    }

    /**
     * 获取网络错误布局
     *
     * @return
     */
    protected open fun getErrorView(): View? {
        if (!::myErrorView.isInitialized) myErrorView =
            inflater.inflate(R.layout.network_exception_layout, null, false)
        return myErrorView
    }

    protected open fun providerAdapter(): BaseQuickAdapter<*, *>? {
        return null
    }

    protected open fun providerRefreshLayout(): SmartRefreshLayout? {
        return null
    }

    /**
     * 对于列表页面,数据条数为0
     */
    /**
     * 对于列表页面,数据条数为0
     */
    /**
     * 显示空buju
     */
    open fun showEmptyView() {
        if (providerAdapter() != null && getEmptyView() != null) {
            getEmptyView()?.let { providerAdapter()?.setEmptyView(it) }
        }
    }

    /**
     * 显示请求出错布局
     */
    open fun showErrorView() {
        if (providerAdapter() != null && getErrorView() != null) {
            getErrorView()?.let { providerAdapter()?.setEmptyView(it) }
        }
    }


    /**
     * 数据加载出错
     */
    open fun showErrorMsg(errorCode: Int, msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            dismissLoadingDialog()
            showToast(msg)
        }
    }
    //*****************************************状态栏***********************************************
    /**
     * 沉浸式状态栏
     *
     * @param titleBar 解决状态栏和titleBar重叠问题
     */
    open fun setStatusBarTranslucent(titleBar: View?) {
        ImmersionBar.with(this)
            .reset()
            .titleBar(titleBar)
            .statusBarDarkFont(false, 0f)
            .init()
    }

    /**
     * 6.0以上白色状态栏黑色文字，6.0以下灰色状态栏白色文字
     */
    open fun setStatusBarWhite() {
        setStatusBarColor(R.color.white)
    }

    open fun setStatusBarColor(@ColorRes statusBarColor: Int) {
        ImmersionBar.with(this)
            .reset()
            .statusBarColor(statusBarColor)
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .init()
    }

    /**
     * Tosat
     *
     * @param message
     */
    open fun showToast(message: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this == null || isDestroyed || isFinishing) {
                return
            }
        } else {
            if (this == null || isFinishing) {
                return
            }
        }
        ToastUtils.show(message)

    }


}