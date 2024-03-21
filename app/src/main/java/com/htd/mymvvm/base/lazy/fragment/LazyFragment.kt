package com.htd.mymvvm.base.lazy.fragment

import androidx.fragment.app.Fragment
import com.htd.mymvvm.utils.LogUtil


/**
 * Description: Androidx 下支持栏加载的fragment
 */
abstract class LazyFragment : Fragment() {
    protected var TAG = javaClass.simpleName
    private var isLoaded = false
    private var isFirstResume = true

    override fun onResume() {
        super.onResume()

        if (isLoaded) {
            excludeLazyLoad()
        }
        if (!isLoaded && !isHidden) {
            LogUtil.i(TAG, "lazyInit:!!!!!!!")
            lazyLoad()
            isLoaded = true
        }

        everyLoad()
        if (!isFirstResume) {
            preLoadAndEveryLoad()
        }
        isFirstResume = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    open fun excludeLazyLoad() {
        LogUtil.i(TAG, "excludeLazy: -------")
    }

    open fun everyLoad() {
        LogUtil.i(TAG, "everyLoad: ---------------------")
    }

    open fun preLoadAndEveryLoad() {

    }

    abstract fun lazyLoad()

}