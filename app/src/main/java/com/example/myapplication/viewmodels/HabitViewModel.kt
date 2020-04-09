package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.Habit
import com.example.myapplication.repositories.HabitsRepo
import java.util.*

class HabitsViewModel : ViewModel() {

    val nameSubstring: MutableLiveData<String> = MutableLiveData()
    val habits: MediatorLiveData<List<Habit>> = MediatorLiveData()

    init {
        habits.addSource(HabitsRepo.habits,  Observer { newHabits ->
            Log.d("tag", "in viewmodel ${newHabits.size}")
            habits.value = newHabits.values.filter { true }
            Log.d("tag", "real size ${habits.value?.size}")

        })
        habits.addSource(nameSubstring, Observer { newNameFilterSubstring ->
            habits.value = HabitsRepo.habits.value?.values?.filter {
                filterHabitsByName(it, newNameFilterSubstring)
            }
        })

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
        Log.d("tag", habits.value?.size.toString())

        //Log.d("tag", habits.value?.get(0)!!.name)

        habits.value = habits.value?.sortedBy { it.creationDate }
        //Log.d("tag", habits.value?.get(0)!!.name)

    }

    fun sortByDateDesc() {
        Log.d("tag", "in sorting")
        Log.d("tag", habits.value?.size.toString())

        //Log.d("tag", habits.value?.get(0)!!.name)

        habits.value = habits.value?.sortedByDescending { it.creationDate }
       // Log.d("tag", habits.value?.get(0)!!.name)

    }

    fun sortByPriorityDesc() {
        habits.value = habits.value?.sortedByDescending { it.priority }
    }

    fun sortByPriority() {
        habits.value = habits.value?.sortedBy { it.priority }
    }
}