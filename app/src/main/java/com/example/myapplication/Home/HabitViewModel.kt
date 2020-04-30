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
    val goodHabits: MediatorLiveData<List<Habit>> = MediatorLiveData()
    val badHabits: MediatorLiveData<List<Habit>> = MediatorLiveData()

    private val allHabits: LiveData<List<Habit>> = App.database.habitDao().getAll()

    init {
        goodHabits.addSource(allHabits) { newHabits ->
            goodHabits.value = newHabits.filter { it.type == HabitType.GOOD }
        }
        badHabits.addSource(allHabits) { newHabits ->
            badHabits.value = newHabits.filter { it.type == HabitType.BAD }
        }
        goodHabits.addSource(nameSubstring) { newNameFilterSubstring ->

            goodHabits.value = allHabits.value?.filter {
                filterHabitsByName(it, newNameFilterSubstring) && it.type == HabitType.GOOD
            }
        }
        badHabits.addSource(nameSubstring) { newNameFilterSubstring ->

            badHabits.value = allHabits.value?.filter {
                filterHabitsByName(it, newNameFilterSubstring) && it.type == HabitType.BAD
            }
        }
    }

    private fun filterHabitsByName(
        habit: Habit,
        newNameFilterSubstring: String? = nameSubstring.value
    ): Boolean {
        return newNameFilterSubstring.isNullOrEmpty() ||
                newNameFilterSubstring.toLowerCase(Locale.getDefault()) in habit.name.toLowerCase(Locale.getDefault())
    }

    fun sortByDateAsc() {
        Log.d("tag", "in sorting")
        goodHabits.value = goodHabits.value?.sortedBy { it.creationDate }
        badHabits.value = badHabits.value?.sortedBy { it.creationDate }
    }

    fun sortByDateDesc() {
        Log.d("tag", "in sorting")
        goodHabits.value = goodHabits.value?.sortedByDescending { it.creationDate }
        badHabits.value = badHabits.value?.sortedByDescending { it.creationDate }
    }
}