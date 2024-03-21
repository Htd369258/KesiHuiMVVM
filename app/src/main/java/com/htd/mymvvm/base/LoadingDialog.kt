package com.htd.mymvvm.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.htd.mymvvm.R

/**
 *
 * 网络加载弹窗
 * Created by htd on 18-7-21.
 */
class LoadingDialog(context: Context) :
    BaseDialog<ViewDataBinding>(context, R.style.AppTheme_CustomDialogTheme) {
    override fun getLayoutResID(): Int = R.layout.dialog_loading

    override fun initView() {

    }
}
