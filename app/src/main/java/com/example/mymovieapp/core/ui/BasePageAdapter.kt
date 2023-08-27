package com.example.mymovieapp.core.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BasePageAdapter(activity: FragmentActivity, private val fragments: Array<Fragment>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(p: Int): Fragment {
        return fragments[p]
    }
}