package com.htd.mymvvm.main.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.htd.mymvvm.base.DataResult
import com.htd.mymvvm.base.MyObserver
import com.htd.mymvvm.base.network.ApiResultCallBack
import com.htd.mymvvm.base.network.reqLaunch
import javax.inject.Inject
import com.htd.mymvvm.base.ui.BaseViewModel
import com.htd.mymvvm.main.adapter.SongAdapter
import com.htd.mymvvm.main.repository.MainRepository
import com.htd.mymvvm.main.response.Song

class SearchSongViewModel @Inject constructor(application: Application) :
    BaseViewModel(application) {

    @Inject
    lateinit var mainRepository: MainRepository

    val songList: MutableLiveData<List<Song>> by lazy {
        MutableLiveData<List<Song>>()
    }

    @Inject
    lateinit var songAdapter: SongAdapter

    private var term: String = ""

    private var isSortByPrice: Boolean = false

    override fun onRefresh() {
        if (term.isEmpty()) {
            postDismissLoadingDialogEvent(true)
            return
        }
        searchSong(term, isSortByPrice,true)
    }

    override fun onLoadMore() {
        if (songAdapter.data.isNullOrEmpty()) {
            postDismissLoadingDialogEvent(true)
            return
        }
        searchSong(term, isSortByPrice,false)
    }

    fun searchSong(
        term: String, isSortByPrice: Boolean,refresh: Boolean
    ) {
        this.term = term
        this.isSortByPrice = isSortByPrice
        loadListData(refresh)
        reqLaunch(
            {
                mainRepository.searchSong(term, "HK", 20,loadMore())
            },
            object : ApiResultCallBack<List<Song>?>() {
                override fun onSuccess(t: List<Song>?) {
                    if (t.isNullOrEmpty()) {
                        postShowEmptyViewEvent(true)
                    } else {
                        songList.value = sort(t, isSortByPrice)
                    }
                }
            },
            true, isNeedShowErrorView = true
        )
    }

    fun sort(songs: List<Song>?, isSortByPrice: Boolean): List<Song>? {
        songs?.let {
            val sortByPrice = it.sortedBy { song ->
                song.trackPrice
            }
            if (isSortByPrice)
                return sortByPrice
            else
                return sortByPrice.sortedBy { song ->
                    song.artistName
                }
        }
        return null
    }


    /**
     * rxjava  的用法
     */
    fun searchSongForRxjava(
        term: String,
        country: String,
        limit: Int, isSortByPrice: Boolean,refresh:Boolean
    ) {
        loadListData(refresh)
        mainRepository.searchSongForRxjava(term, country, limit,loadMore())
            .map {
                return@map it.results
            }
            .subscribe(object : MyObserver<List<Song>?>(this) {
                override fun onNext(t: List<Song>) {
                    if (t.isNullOrEmpty()) {
                        postShowEmptyViewEvent(true)
                    } else {

                        songAdapter.setList(
                            sort(t, isSortByPrice)
                        )

                    }
                }

            })
    }

}
