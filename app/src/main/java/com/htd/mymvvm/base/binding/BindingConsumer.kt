package com.htd.mymvvm.base.binding

/**
 * A one-argument action.
 *
 * @param <T> the first argument type
</T> */
interface BindingConsumer<T> {
    fun call(t: T)
}