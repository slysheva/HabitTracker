package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton



class HabitListFragment : Fragment() {

    private var adapter: HabitsAdapter? = null
    private var habitType = ""
    var callback: onHabitChengeRequestListenner? = null

    companion object {
        const val  GOOD_HABITS = "GOOD_HABITS"
        const val BAD_HABITS = "BAD_HABITS"
        private const val HABIT_TYPE = "HABIT_TYPE"

        fun newInstance(habitType: String): HabitListFragment {
            return HabitListFragment().apply { arguments = bundleOf(HABIT_TYPE to habitType) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as onHabitChengeRequestListenner
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
            habitType = it.getString(HABIT_TYPE, GOOD_HABITS)
        }
        val habitsList = view.findViewById<RecyclerView>(R.id.recyclerview)
        habitsList.layoutManager = LinearLayoutManager(activity)
        Log.d(this::class.java.canonicalName, "new list created ${MainActivity.habits.size}" )
        if (MainActivity.habits.size > 0)
            Log.d(this::class.java.canonicalName, "first habit: ${MainActivity.habits[0].type}" )

        adapter =when (habitType) {
            GOOD_HABITS -> HabitsAdapter(activity!!,
                MainActivity.habits.filter{ habit -> habit.type == HabitType.GOOD } as MutableList<Habit>) { itemClicked, pos ->
                callback?.habitChangeRequest(itemClicked, pos, MainActivity.HABIT_EDIT_REQUEST)
                adapter?.notifyDataSetChanged()
            }
            else -> HabitsAdapter(activity!!,
                MainActivity.habits.filter{ habit -> habit.type == HabitType.BAD } as MutableList<Habit>) { itemClicked, pos ->
                callback?.habitChangeRequest(itemClicked, pos, MainActivity.HABIT_EDIT_REQUEST)
                adapter?.notifyDataSetChanged()
            }
        }
        habitsList.adapter = adapter
    }

    interface onHabitChengeRequestListenner {
        fun habitChangeRequest(habit: Habit?, pos: Int?, status: Int)
    }



    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

}
