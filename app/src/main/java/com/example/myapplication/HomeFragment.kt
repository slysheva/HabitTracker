package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home_pager.*


class HomeFragment : Fragment() {
    var callback: HabitListFragment.HabitChangeRequestListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pager, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListFragment.HabitChangeRequestListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager_home_fragment.adapter = HomePagerAdapter(this)

        TabLayoutMediator(tab_layout, view_pager_home_fragment) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.bad)
                else -> getString(R.string.good)
            }
        }.attach()

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            callback?.onHabitChangeRequest(null, null, MainActivity.HABITS_ADD_REQUEST)
        }
    }
}
