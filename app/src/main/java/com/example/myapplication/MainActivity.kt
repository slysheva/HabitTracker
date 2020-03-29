package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private var habits = mutableListOf<Habit>()
    private var adapter: HabitsAdapter? = null
    private val HABITS_REQUEST = 0
    private val HABITS_ADD_REQUEST = 0
    private val HABIT_EDIT_REQUEST = 1
    private val HABIT_REMOVE_REQUEST = 2

    companion object {
        const val HABIT_STRING = "HABIT"
        const val ID_STRING = "ID"
        const val HABITS_STRING = "HABITS"
    }

    override fun onStart() {
        super.onStart()
        Log.d(this::class.java.canonicalName, "Started with value: $HABITS_REQUEST")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(this::class.java.canonicalName, "Restarted with value: $HABITS_REQUEST" )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this::class.java.canonicalName, "Created with value: $HABITS_REQUEST" )
        setContentView(R.layout.activity_main)

        val habitsList = findViewById<RecyclerView>(R.id.recyclerview)
        habitsList.layoutManager = LinearLayoutManager(this)

        adapter = HabitsAdapter(this, habits) { itemClicked, pos ->
            val sendIntent = Intent(this, NewHabitActivity::class.java).apply {
                val bundle = Bundle().apply{
                    putSerializable(HABIT_STRING, itemClicked)
                    putInt(ID_STRING, pos)
                }
                putExtras(bundle)
            }
            startActivityForResult(sendIntent, HABIT_EDIT_REQUEST)
        }

        habitsList.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            val sendIntent = Intent(this, NewHabitActivity::class.java)
            startActivityForResult(sendIntent, HABITS_ADD_REQUEST)
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val newHabit = data.getSerializableExtra(HABIT_STRING) as Habit

                if (requestCode == HABIT_EDIT_REQUEST) {
                    val id = data.extras!!.getInt(ID_STRING)
                    habits[id] = newHabit
                }
                else if(requestCode == HABITS_ADD_REQUEST && !habits.contains(newHabit)) {
                    habits.add(newHabit)
                }
            }
        }
        adapter?.notifyDataSetChanged()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.apply{putSerializable(HABITS_STRING, habits as Serializable)}
        super.onSaveInstanceState(outState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        habits = savedInstanceState.getSerializable(HABITS_STRING) as MutableList<Habit>
        habits.sortByDescending { habit -> habit.priority }
        adapter = HabitsAdapter(this, habits) { itemClicked, pos ->
            val sendIntent = Intent(this, NewHabitActivity::class.java).apply {
                val bundle = Bundle().apply{
                    putSerializable(HABIT_STRING, itemClicked)
                    putInt(ID_STRING, pos)
                }
                putExtras(bundle)
            }
            startActivityForResult(sendIntent, HABIT_EDIT_REQUEST)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}

