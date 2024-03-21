package com.htd.mymvvm.base.dagger.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.multibindings.IntoMap
import com.htd.mymvvm.base.dagger.MyViewModelFactory
import com.htd.mymvvm.base.dagger.ViewModelKey
import com.htd.mymvvm.main.view_model.AViewModel
import com.htd.mymvvm.main.view_model.MainViewModel
import com.htd.mymvvm.main.view_model.SearchSongViewModel
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AViewModel::class)
    abstract fun bindAViewModel(aViewModel: AViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchSongViewModel::class)
    abstract fun bindSearchSongViewModel(searchSongViewModel: SearchSongViewModel): ViewModel
}
