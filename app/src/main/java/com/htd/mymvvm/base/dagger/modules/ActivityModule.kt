package com.htd.mymvvm.base.dagger.modules

import com.htd.mymvvm.main.activity.AActivity
import com.htd.mymvvm.main.activity.MainActivity
import com.htd.mymvvm.main.activity.SearchSongActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun contributeAActivity(): AActivity

    @ContributesAndroidInjector()
    abstract fun contributeSearchSongActivity(): SearchSongActivity
}
