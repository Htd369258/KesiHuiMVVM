package com.htd.mymvvm.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.htd.mymvvm.R
import com.htd.mymvvm.databinding.ItemSongBinding
import com.htd.mymvvm.main.response.Song
import javax.inject.Inject

class SongAdapter
@Inject constructor() :
    BaseQuickAdapter<Song, BaseDataBindingHolder<ItemSongBinding>>(R.layout.item_song) {
    override fun convert(holder: BaseDataBindingHolder<ItemSongBinding>, item: Song) {
        holder?.dataBinding?.song = item
        holder?.dataBinding?.executePendingBindings()
    }
}