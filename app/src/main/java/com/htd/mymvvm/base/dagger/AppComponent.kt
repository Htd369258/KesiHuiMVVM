package com.htd.mymvvm.base.dagger

import android.app.Application

import com.htd.mymvvm.App
import com.htd.mymvvm.base.dagger.modules.ActivityModule
import com.htd.mymvvm.base.dagger.modules.FragmentBuildersModule
import com.htd.mymvvm.base.dagger.modules.FragmentProviderModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        ActivityModule::class,
        FragmentProviderModule::class,
        FragmentBuildersModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(githubApp: App)
}
