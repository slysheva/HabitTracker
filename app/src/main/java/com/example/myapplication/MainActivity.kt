package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity(), NewHabitFragment.OnHabitSelectedListener, HabitListFragment.onHabitChengeRequestListenner, NavigationView.OnNavigationItemSelectedListener  {

    companion object {
        const val HABITS_STRING = "HABITS"
        const val HABITS_ADD_REQUEST = 0
        const val HABIT_EDIT_REQUEST = 1
        const val HABIT_REMOVE_REQUEST = 2
        var habits = mutableListOf<Habit>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this::class.java.canonicalName, "in onCreate" )
        setContentView(R.layout.activity_main)
        Log.d(this::class.java.canonicalName, "kek" )

//        val navController = this.findNavController(R.id.navigation_fragment)
//        NavigationUI.setupActionBarWithNavController(this, navController, navigation_drawer)
//        NavigationUI.setupWithNavController(navigation_view, navController)

        val drawerToggle =
            ActionBarDrawerToggle(this,
                navigation_drawer,
                R.string.open_drawer,
                R.string.close_drawer)
        navigation_drawer.addDrawerListener(drawerToggle)

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, homeFragment!!, "homeFragment")
            .commit()
        navigation_view.setNavigationItemSelectedListener(this)

        Log.d(this::class.java.canonicalName, "lol" )

    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.navigation_fragment)
//        return NavigationUI.navigateUp(navController, navigation_drawer)
//    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_home -> {
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment !is NavHostFragment) {
                        supportFragmentManager.beginTransaction().remove(fragment).commit()

                    }
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, HomeFragment(), "homeFragment")
                    .commit()
                Log.d(this::class.java.canonicalName, "home item clicked" )
            }
            R.id.menu_item_about -> {
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment !is NavHostFragment)
                        supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, AboutFragment(), "aboutFragment")
                    .commit()

                Log.d(this::class.java.canonicalName, "about item clicked" )
            }
        }
        navigation_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onHabitChange(habit: Habit?, pos: Int?, statusCode: Int?) {
        if (habit != null) {
            Log.d(this::class.java.canonicalName, "in callback, $statusCode, ${habit.name}, $pos" )
        }
        if (statusCode == HABIT_EDIT_REQUEST && pos != null && habit != null) {
            habits[pos] = habit
        }
        else if(statusCode == HABITS_ADD_REQUEST) {
            if (habit != null) {
                habits.add(habit)
            }

            Log.d(this::class.java.canonicalName, "added, cur size: ${habits.size}" )

        }
        if (habits.size > 0)
            Log.d(this::class.java.canonicalName, "first habit: ${habits[0].type}" )
        supportFragmentManager.findFragmentByTag("newHabitFragment")?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .add(R.id.fragment_container, HomeFragment(), "homeFragment")
                .commit()
            Log.d(this::class.java.canonicalName, "removed" )
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.apply{putSerializable(HABITS_STRING, habits as Serializable)}
        super.onSaveInstanceState(outState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        habits = savedInstanceState.getSerializable(HABITS_STRING) as MutableList<Habit>
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun habitChangeRequest(habit: Habit?, pos: Int?, status: Int) {
        var newPos: Int? = null
        if (habit != null)
            newPos = habits.indexOf(habit)
        val fragment = NewHabitFragment.newInstance(habit, newPos, status)
        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.findFragmentByTag( "homeFragment")!!)
            .add(R.id.fragment_container, fragment, "newHabitFragment")
            .commit()
    }


}

