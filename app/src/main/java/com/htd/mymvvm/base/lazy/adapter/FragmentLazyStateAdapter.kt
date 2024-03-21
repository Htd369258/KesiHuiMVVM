package com.htd.mymvvm.base.lazy.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * Description:
 */
class FragmentLazyStateAdapter(
    fragment: Fragment,
    private val fragments: MutableList<Fragment>
) :
    FragmentStateAdapter(fragment) {

    fun getDatas(): MutableList<Fragment> {
        return fragments
    }

    fun getItemData(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}