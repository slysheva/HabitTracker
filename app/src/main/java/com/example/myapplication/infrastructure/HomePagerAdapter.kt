package com.example.myapplication.infrastructure


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.Home.HabitListFragment

class HomePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val tabNumber = 2

    override fun getItemCount(): Int {
        return tabNumber
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabitListFragment.newInstance(
                HabitListFragment.BAD_HABITS)
            else -> HabitListFragment.newInstance(
                HabitListFragment.GOOD_HABITS)
        }
    }
}