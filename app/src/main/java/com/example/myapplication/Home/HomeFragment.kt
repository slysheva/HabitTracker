package com.example.myapplication.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.infrastructure.HomePagerAdapter
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_home_pager.*


class HomeFragment : Fragment() {
    lateinit var navController: NavController
    private val viewModel: HabitsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_pager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager_home_fragment.adapter =
            HomePagerAdapter(this)

        TabLayoutMediator(tab_layout, view_pager_home_fragment) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.bad)
                else -> getString(R.string.good)
            }
        }.attach()
        navController = findNavController()

        nameFilter.addTextChangedListener {
            viewModel.nameSubstring.value = it.toString()
        }

        sortAsc.setOnClickListener {
            viewModel.sortByDateAsc()
        }

        sortDesc.setOnClickListener {
            viewModel.sortByDateDesc()
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_newHabitFragment)
        }
    }
}
