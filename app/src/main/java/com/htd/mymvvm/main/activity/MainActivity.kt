package com.htd.mymvvm.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.htd.mymvvm.BR
import com.htd.mymvvm.R

import com.htd.mymvvm.base.ui.BaseActivity
import com.htd.mymvvm.databinding.ActivityMainBinding
import com.htd.mymvvm.main.view_model.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun onBindViewModel(): Class<MainViewModel> =
        MainViewModel::class.java

    override fun initViewModelVariableId(): Int = BR.viewModel

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initClickListener() {
        val  meettiings1= mutableListOf<List<Int>>(
            listOf(16,20),
            listOf(5,10),
            listOf(11,12),
            listOf(8,15))
        val  meettiings2= mutableListOf<List<Int>>(
            listOf(1,3),
            listOf(6,10),
            listOf(4,5),
            listOf(11,15))

        viewDataBinding.calculate1.setOnClickListener {

            viewDataBinding.result1.text=  "会议数据：${meettiings1.toMutableList()} ，一个人是否可以参加完所有会议结果为：${viewModel.isAttendAllMeetings(meettiings1)}  "
        }

        viewDataBinding.calculate2.setOnClickListener {
            viewDataBinding.result1.text=  "会议数据：${meettiings2.toMutableList()} ，一个人是否可以参加完所有会议结果为：${viewModel.isAttendAllMeetings(meettiings2)}  "
        }
        viewDataBinding.click.setOnClickListener {
            SearchSongActivity.launch(this)
        }
    }
    override fun getLayoutResID(): Int = R.layout.activity_main

    companion object {
        @JvmOverloads
        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
