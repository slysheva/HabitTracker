package com.example.myapplication.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Habit

class HabitsRepo {
    companion object {
        private val mutableHabits: MutableLiveData<MutableMap<Int, Habit>> = MutableLiveData(
            mutableMapOf()
        )

        val habits: LiveData<MutableMap<Int, Habit>> = mutableHabits

        fun addHabit(habit: Habit) {
            val newHabit= Habit(
                mutableHabits.value?.size!!,
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.quantity,
                habit.periodicity,
                habit.creationDate
            )

            mutableHabits.value?.set(newHabit.id, newHabit);
            Log.d("tag", "added, size ${mutableHabits.value?.size}")
        }

        fun removeHabit(habitId: Int) {
            if (mutableHabits.value?.containsKey(habitId)!!)
                mutableHabits.value?.remove(habitId);
        }

        fun updateHabit(id: Int, habit: Habit) {
            mutableHabits.value?.set(id, habit);
        }

        fun loadHabit(id: Int): Habit? {
            return mutableHabits.value?.get(id);
        }
    }
}