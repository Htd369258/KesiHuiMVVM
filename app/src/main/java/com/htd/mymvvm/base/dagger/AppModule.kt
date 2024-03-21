package com.htd.mymvvm.base.dagger


import android.app.Application
import android.content.Context
import com.htd.mymvvm.base.dagger.modules.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    //
//    /**
//     * 提供context
//     * @return
//     */
    @Singleton
    @Provides
    fun provideContext(context: Application): Context {
        return context
    }

}
