package com.htd.mymvvm.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.htd.mymvvm.R
import com.htd.mymvvm.base.dagger.Injectable
import com.htd.mymvvm.base.lazy.fragment.LazyFragment
import com.scwang.smart.refresh.layout.SmartRefreshLayout

import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : LazyFragment(), Injectable {

    lateinit var viewDataBinding: V

    lateinit var myEmptyView: View

    lateinit var myErrorView: View

    open val baseActivity: BaseActivity<*, *> by lazy {
        activity as BaseActivity<*, *>
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract fun onBindViewModel(): Class<VM>

    override fun lazyLoad() {

    }

    open val viewModel: VM by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory).get(onBindViewModel())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutResID(),
            container,
            false
        )
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
        viewDataBinding.setVariable(initViewModelVariableId(), viewModel)
        //支持LiveData绑定xml，数据改变，UI自动会更新
        viewDataBinding.lifecycleOwner = this
        initUiChangeEvent()
        initViewObservable()
        initView(savedInstanceState)
        initClickListener()
        preLoadAndEveryLoad()
    }

    /**
     * 注册eventbus
     */
    open fun isRegisterEventBus(): Boolean {
        return false
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
        viewDataBinding.unbind()
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    private fun initUiChangeEvent() {

        viewModel.getUIChangeLiveDataEvent().showLoadingNowEvent.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                baseActivity.showLoadingDialogNow(true)
            } else {
                baseActivity.showLoadingDialog()
            }
        }
        viewModel.getUIChangeLiveDataEvent().dismissLoadingDialogEvent.observe(
            viewLifecycleOwner
        ) {
            dismissLoadingDialog()
        }
        viewModel.getUIChangeLiveDataEvent().showToastMessageEvent.observe(
            viewLifecycleOwner
        ) {
            baseActivity.showToast(it)
        }
        viewModel.getUIChangeLiveDataEvent().showEmptyViewEvent.observe(
            viewLifecycleOwner
        ) {
            if (it) showEmptyView(it)
        }
        viewModel.getUIChangeLiveDataEvent().showNetworkErrorEvent.observe(
            viewLifecycleOwner
        ) {
            if (it) showErrorView(it)
        }
    }

    /**
     * 获取列表的空布局
     *
     * @return
     */
    protected open fun getEmptyView(): View? {
        if (!::myEmptyView.isInitialized) myEmptyView =
            baseActivity.inflater.inflate(R.layout.empty_layout, null, false)
        return myEmptyView
    }

    /**
     * 获取网络错误布局
     *
     * @return
     */
    protected open fun getErrorView(): View? {
        if (!::myErrorView.isInitialized) myErrorView =
            baseActivity.inflater.inflate(R.layout.network_exception_layout, null, false)
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
    open fun showEmptyView(isShow: Boolean) {
        if (providerAdapter() != null && getEmptyView() != null && isShow) {
            getEmptyView()?.let { providerAdapter()?.setEmptyView(it) }
        }
    }

    /**
     * 显示请求出错布局
     */
    open fun showErrorView(isShow: Boolean) {
        if (providerAdapter() != null && getErrorView() != null && isShow) {
            getErrorView()?.let { providerAdapter()?.setEmptyView(it) }
        }
    }

    /**
     * 取消加载对话框
     */
    open fun dismissLoadingDialog() {
        baseActivity.dismissLoadingDialog()
        dismissRefreshLayout()
    }

    open fun dismissRefreshLayout() {
        providerRefreshLayout()?.finishRefresh()
        providerRefreshLayout()?.finishLoadMore()
    }

    @LayoutRes
    protected abstract fun getLayoutResID(): Int


    /**
     * 初始化UI 相当onCreate方法
     *
     * @param savedInstanceState
     */
    protected abstract fun initView(savedInstanceState: Bundle?)


}