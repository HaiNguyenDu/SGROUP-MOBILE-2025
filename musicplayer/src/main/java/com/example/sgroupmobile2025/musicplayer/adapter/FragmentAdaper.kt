package com.example.sgroupmobile2025.musicplayer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_COUNT
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_DETAIL
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_HOME
import com.example.sgroupmobile2025.musicplayer.ui.DetailFragment
import com.example.sgroupmobile2025.musicplayer.ui.FavouriteFragment
import com.example.sgroupmobile2025.musicplayer.ui.HomeFragment

class FragmentAdaper(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            FRAGMENT_HOME -> HomeFragment()
            FRAGMENT_DETAIL -> DetailFragment()
            else -> FavouriteFragment()
        }
    }

    override fun getItemCount(): Int = FRAGMENT_COUNT
}