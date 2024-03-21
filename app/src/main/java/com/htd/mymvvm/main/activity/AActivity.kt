package com.htd.mymvvm.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.htd.mymvvm.BR
import com.htd.mymvvm.R

import com.htd.mymvvm.base.ui.BaseActivity
import com.htd.mymvvm.databinding.ActivityABinding
import com.htd.mymvvm.main.view_model.AViewModel

class AActivity : BaseActivity<ActivityABinding, AViewModel>() {
    override fun onBindViewModel(): Class<AViewModel> =
        AViewModel::class.java

    override fun initViewModelVariableId(): Int = BR.viewModel

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun getLayoutResID(): Int = R.layout.activity_a

    companion object {
        @JvmOverloads
        fun launch(context: Context) {
            context.startActivity(Intent(context, AActivity::class.java))
        }
    }
}
