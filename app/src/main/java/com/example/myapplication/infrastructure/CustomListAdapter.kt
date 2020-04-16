package com.example.myapplication.infrastructure

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.repositories.database.Habit
import com.example.myapplication.HabitType
import com.example.myapplication.R


class HabitsAdapter(private val context: Context, private var habits: MutableList<Habit>, val clickListener: (Habit, Int) -> Unit)
    : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(habits[position],position,  clickListener)
    }
    fun updateHabits(newHabits: MutableList<Habit>){
        habits = newHabits
    }
}


class ViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(habit: Habit, pos: Int, clickListener: (Habit, Int) -> Unit) {
        habitTitle.text = habit.name
        habitDescription.text = habit.description
        habitType.text = habit.type.toString()
        habitPriority.text = "priority: " + habit.priority.toString()
        habitPriority.text = habit.priority.toString()
        habitPeriod.text = habit.periodicity.toString()
        habitQuantity.text = habit.quantity.toString()

        if (habit.type == HabitType.GOOD)
            imageView.setImageResource(R.mipmap.habit_type_bad_good)
        else
            imageView.setImageResource(R.mipmap.habit_type_bad)

        view.setOnClickListener { clickListener(habit, pos)}
    }

    private val imageView = view.findViewById(R.id.imageView2) as ImageView
    private val habitTitle = view.findViewById(R.id.habitTitle) as TextView
    private val habitDescription = view.findViewById(R.id.habitDescription) as TextView
    private val habitType = view.findViewById(R.id.habitType) as TextView
    private val habitPriority = view.findViewById(R.id.habitPriority) as TextView
    private val habitPeriod = view.findViewById(R.id.habitPeriod) as TextView
    private val habitQuantity = view.findViewById(R.id.habitQuantity) as TextView
}

