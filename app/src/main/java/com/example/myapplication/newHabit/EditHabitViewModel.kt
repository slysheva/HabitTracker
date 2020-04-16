package com.example.myapplication.newHabit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.repositories.database.Habit
import com.example.myapplication.repositories.inmemory.HabitsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditHabitViewModel(private val habitId: Int) : ViewModel() {
    private val mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> =  App.database.habitDao().getById(habitId)


    fun saveHabit(habit: Habit) {
        val habitDao = App.database.habitDao()
        viewModelScope.launch {
            if (habitId == -1) {
                habitDao.insert(habit)
            } else {
                habit.id = habitId
                habitDao.update(habit)
            }
        }
    }

    fun deleteHabit() {
        val habitDao = App.database.habitDao()
        viewModelScope.launch {
            habitDao.delete(habit.value!!)
        }
    }
}