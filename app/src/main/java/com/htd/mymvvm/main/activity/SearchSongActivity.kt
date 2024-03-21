package com.htd.mymvvm.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.htd.mymvvm.BR
import com.htd.mymvvm.R
import com.htd.mymvvm.base.network.setListData

import com.htd.mymvvm.base.ui.BaseActivity
import com.htd.mymvvm.databinding.ActivitySearchSongBinding
import com.htd.mymvvm.main.adapter.SongAdapter
import com.htd.mymvvm.main.response.Song
import com.htd.mymvvm.main.view_model.SearchSongViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import javax.inject.Inject

class SearchSongActivity : BaseActivity<ActivitySearchSongBinding, SearchSongViewModel>() {

    override fun onBindViewModel(): Class<SearchSongViewModel> =
        SearchSongViewModel::class.java

    override fun initViewModelVariableId(): Int = BR.viewModel

    override fun providerAdapter(): BaseQuickAdapter<*, *> =  viewModel.songAdapter

    override fun initView(savedInstanceState: Bundle?) {
        viewDataBinding.rv.adapter = viewModel.songAdapter
        viewDataBinding.rv.layoutManager = LinearLayoutManager(this.application)

    }

    override fun initViewObservable() {
        viewModel.songList.observe(this){
            viewModel.songAdapter.setListData(viewModel, it)
        }
    }

    override fun initClickListener() {
        viewDataBinding.search.setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                var term=viewDataBinding.search.text.toString()
                if(term.isEmpty()) {
                    showToast("请输入内容")
                    return@setOnEditorActionListener false
                }
                viewModel.searchSong(term,viewDataBinding.rbSortByPrice.isChecked,true)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        viewDataBinding.rg.setOnCheckedChangeListener { group, checkedId ->
            sort()
        }
    }

    private fun sort() {
        if (viewModel.songAdapter.data.isEmpty())
            return
        viewModel.songAdapter.setList(viewModel.sort(viewModel.songAdapter.data,  viewDataBinding.rbSortByPrice.isChecked))
    }

    override fun providerRefreshLayout(): SmartRefreshLayout = viewDataBinding.refresh

    override fun getLayoutResID(): Int = R.layout.activity_search_song

    companion object {
        @JvmOverloads
        fun launch(context: Context) {
            context.startActivity(Intent(context, SearchSongActivity::class.java))
        }
    }
}
