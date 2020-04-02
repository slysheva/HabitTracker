package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity(), NewHabitFragment.OnHabitSelectedListener, HabitListFragment.HabitChangeRequestListener  {

    companion object {
        const val HABIT_STRING = "HABIT"
        const val ID_STRING = "ID"
        const val STATUS_STRING = "STATUS"
        const val HABITS_STRING = "HABITS"
        const val HABITS_ADD_REQUEST = 0
        const val HABIT_EDIT_REQUEST = 1
        const val HABIT_REMOVE_REQUEST = 2
        var habits = mutableListOf<Habit>()
    }
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = this.findNavController(R.id.navigation_fragment)

        NavigationUI.setupActionBarWithNavController(this, navController, navigation_drawer)
        NavigationUI.setupWithNavController(navigation_view, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navigation_fragment)
        return NavigationUI.navigateUp(navController, navigation_drawer)
    }

    override fun onHabitChange(habit: Habit, pos: Int?, statusCode: Int?) {
        when (statusCode) {
            HABIT_EDIT_REQUEST -> habits[pos!!] = habit
            HABITS_ADD_REQUEST -> habits.add(habit)
            HABIT_REMOVE_REQUEST -> habits.removeAt(pos!!)
        }

        navController.navigate(R.id.action_newHabitFragment_to_homeFragment)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.apply{putSerializable(HABITS_STRING, habits as Serializable)}
        super.onSaveInstanceState(outState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        habits = savedInstanceState.getSerializable(HABITS_STRING) as MutableList<Habit>
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onHabitChangeRequest(habit: Habit?, pos: Int?, status: Int) {
        var newPos: Int = -1
        if (habit != null)
            newPos = habits.indexOf(habit)

        val bundle = Bundle().apply{
            putSerializable(HABIT_STRING, habit)
            putInt(ID_STRING, newPos)
            putInt(STATUS_STRING, status)
        }
        navController.navigate(R.id.action_homeFragment_to_newHabitFragment, bundle)
    }
}
