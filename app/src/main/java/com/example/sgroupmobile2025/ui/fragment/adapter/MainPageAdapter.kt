package com.example.sgroupmobile2025.ui.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sgroupmobile2025.ui.fragment.AddFragment
import com.example.sgroupmobile2025.ui.fragment.MainFragment

class MainPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {

        return when (position) {
            FRAGMENT_MAIN -> MainFragment()
            FRAGMENT_ADD_IMAGE -> AddFragment()
            else -> MainFragment()
        }
    }

    override fun getItemCount() = FRAGMENT_COUNT

    companion object {
        const val FRAGMENT_MAIN = 0
        const val FRAGMENT_ADD_IMAGE = 1

        const val FRAGMENT_COUNT = 2
    }
}