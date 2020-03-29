package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class HabitsAdapter(val context: Context, private val habits: MutableList<Habit>, val clickListener: (Habit, Int) -> Unit)
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

    val imageView = view.findViewById(R.id.imageView2) as ImageView
    val habitTitle = view.findViewById(R.id.habitTitle) as TextView
    val habitDescription = view.findViewById(R.id.habitDescription) as TextView
    val habitType = view.findViewById(R.id.habitType) as TextView
    val habitPriority = view.findViewById(R.id.habitPriority) as TextView
    val habitPeriod = view.findViewById(R.id.habitPeriod) as TextView
    val habitQuantity = view.findViewById(R.id.habitQuantity) as TextView
}

