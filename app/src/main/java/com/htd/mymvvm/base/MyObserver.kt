package com.htd.mymvvm.base

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import com.htd.mymvvm.base.ui.BaseViewModel

abstract class MyObserver<T> constructor(baseViewModel: BaseViewModel) : Observer<T> {
    private var baseViewModel: BaseViewModel = baseViewModel

    private lateinit var d: Disposable

    override fun onSubscribe(d: Disposable) {
        this.d = d
        baseViewModel.addTask(d)
    }

    override fun onError(e: Throwable) {
        baseViewModel.loadError(e)
    }

    override fun onComplete() {
        d.let {
            baseViewModel.removeTask(it)
        }
        baseViewModel.loadComplete()
    }

}
