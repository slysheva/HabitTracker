package com.example.myapplication.Home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.App
import com.example.myapplication.HabitType
import com.example.myapplication.repositories.database.Habit
import com.example.myapplication.repositories.inmemory.HabitsRepo
import java.util.*

class HabitsViewModel : ViewModel() {

    val nameSubstring: MutableLiveData<String> = MutableLiveData()
    val habits: MediatorLiveData<List<Habit>> = MediatorLiveData()
    private val allHabits: LiveData<List<Habit>> = App.database.habitDao().getAll()


    init {
        habits.addSource(allHabits) { newHabits ->
            Log.d("tag", "in onchange ${newHabits.size}")
            habits.value = newHabits.toList()
        }
        habits.addSource(nameSubstring) { newNameFilterSubstring ->
            Log.d("tag", "in wrong onchange ")

            habits.value = allHabits.value?.filter {
                filterHabitsByName(it, newNameFilterSubstring)
            }
        }

    }

    private fun filterHabitsByName(
        habit: Habit,
        newNameFilterSubstring: String? = nameSubstring.value
    ): Boolean {
        return newNameFilterSubstring.isNullOrEmpty() ||
                newNameFilterSubstring.toLowerCase(Locale.ROOT) in habit.name.toLowerCase(Locale.ROOT)
    }

    fun sortByDateAsc() {
        Log.d("tag", "in sorting")

        habits.value = habits.value?.sortedBy { it.creationDate }
    }

    fun sortByDateDesc() {
        Log.d("tag", "in sorting")
        habits.value = habits.value?.sortedByDescending { it.creationDate }
    }

    fun sortByPriorityDesc() {
        habits.value = habits.value?.sortedByDescending { it.priority }
    }

    fun sortByPriority() {
        habits.value = habits.value?.sortedBy { it.priority }
    }
}