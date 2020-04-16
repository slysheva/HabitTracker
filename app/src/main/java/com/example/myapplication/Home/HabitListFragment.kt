package com.example.myapplication.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.*
import com.example.myapplication.infrastructure.HabitsAdapter
import kotlinx.android.synthetic.main.home_fragment.*


class HabitListFragment : Fragment() {

    private var habitType = ""
    lateinit var navController: NavController
    private val viewModel: HabitsViewModel by activityViewModels()
    private var listAdapter: HabitsAdapter? = null

    companion object {
        const val  GOOD_HABITS = "GOOD_HABITS"
        const val BAD_HABITS = "BAD_HABITS"
        private const val HABIT_TYPE = "HABIT_TYPE"

        fun newInstance(habitType: String): HabitListFragment {
            return HabitListFragment()
                .apply { arguments = bundleOf(HABIT_TYPE to habitType) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            habitType = it.getString(
                HABIT_TYPE,
                GOOD_HABITS
            )
        }
        navController = findNavController()
        recyclerview.adapter = HabitsAdapter(context!!, mutableListOf()){ itemClicked, _ ->
                                    val bundle = Bundle().apply {
                            putInt(
                                MainActivity.ID_STRING,
                                itemClicked.id!!
                            )
                        }

                        navController.navigate(
                            R.id.action_homeFragment_to_newHabitFragment,
                            bundle
                        )
                    }
        recyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.habits.observe(viewLifecycleOwner, Observer { habits ->
            Log.d("habits size", habits.size.toString())
            (recyclerview.adapter as HabitsAdapter).updateHabits(
                when (habitType) {
                    GOOD_HABITS -> habits.filter { it.type == HabitType.GOOD } as MutableList
                    else -> habits.filter { it.type == HabitType.BAD } as MutableList
                }
            )

            (recyclerview.adapter as HabitsAdapter).notifyDataSetChanged()
        })

    }
}

