package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Habit
import com.example.myapplication.repositories.HabitsRepo

class EditHabitViewModel(private val habitId: Int) : ViewModel() {
    private val mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> = mutableHabit

    init {
        load()
    }

    private fun load() {
        HabitsRepo.loadHabit(habitId).let { loadedHabit: Habit? ->
            mutableHabit.postValue(loadedHabit)
        }
    }

    fun saveHabit(habit: Habit) {
        if (habitId == -1) {
            HabitsRepo.addHabit(habit)
        } else {
            HabitsRepo.updateHabit(habitId, habit)
        }
    }

    fun deleteHabit() {
        HabitsRepo.removeHabit(habitId)
    }
}