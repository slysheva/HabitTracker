package com.example.myapplication.Home

import android.content.Context
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

        viewModel.habits.observe(viewLifecycleOwner, Observer { new_habits ->
            Log.d("tag", "in observe")
            updateRecyclerView(new_habits)
        })

    }

    private fun updateRecyclerView(habits: List<Habit>) {
            recyclerview.apply {
                val filteredHabits = habits.filter {
                    when (habitType) {
                        GOOD_HABITS ->  it.type == HabitType.GOOD
                        BAD_HABITS -> it.type == HabitType.BAD
                        else -> true
                    }
                }
                Log.d("tag", "list size ${habits.size} $habitType")
                layoutManager = LinearLayoutManager(context)
                adapter = HabitsAdapter(
                    context,
                    filteredHabits as MutableList<Habit>
                ) { itemClicked, _ ->
                    val bundle = Bundle().apply {
                        putInt(
                            MainActivity.ID_STRING,
                            itemClicked.id
                        )
                    }

                    navController.navigate(
                        R.id.action_homeFragment_to_newHabitFragment,
                        bundle
                    )
                }
            }
    }
}
