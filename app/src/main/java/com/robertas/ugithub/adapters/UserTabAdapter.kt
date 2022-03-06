package com.robertas.ugithub.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.robertas.ugithub.views.FollowerListFragment
import com.robertas.ugithub.views.FollowingListFragment

class UserTabAdapter(fragment: Fragment, private val username: String) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowerListFragment.newInstance(username)
            else -> FollowingListFragment.newInstance(username)
        }
    }
}