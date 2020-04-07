package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.Habit
import com.example.myapplication.repositories.HabitsRepo

class HabitsViewModel : ViewModel() {

    val habits: MediatorLiveData<List<Habit>> = MediatorLiveData()

    init {
        habits.addSource(HabitsRepo.habits,  Observer { newHabits ->
            Log.d("tag", "in viewmodel ${newHabits.size}")
            habits.value = newHabits.values.filter { true }
        })

    }

    fun sortByPriorityDesc() {
        habits.value = habits.value?.sortedByDescending { it.priority }
    }

    fun sortByPriority() {
        habits.value = habits.value?.sortedBy { it.priority }
    }
}