package com.htd.mymvvm.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htd.mymvvm.R

abstract class BaseDialog<V : ViewDataBinding>(
    context: Context,
    @StyleRes style: Int = R.style.AppTheme_MyDialog
) :
    Dialog(context, style) {

    lateinit var viewDataBinding: V

    @LayoutRes
    protected abstract fun getLayoutResID(): Int

    protected abstract fun initView()

    protected open fun initClickListener() {}

    protected open fun setWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    protected open fun setHeight(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    protected open fun setGravity(): Int = Gravity.CENTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutResID(), null, false)
        window?.let {
            val params = it.attributes
            params.width = setWidth()
            params.height = setHeight()
            params.gravity = setGravity()
            it.attributes = params
        }
        setContentView(viewDataBinding.root)

        initView()
        initClickListener()
    }
}