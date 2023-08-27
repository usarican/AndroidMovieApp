package com.example.mymovieapp.core.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class
BasePageAdapterForFragment(fragment: Fragment, private val fragments: Array<Fragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(p: Int): Fragment {
        return fragments[p]
    }
}