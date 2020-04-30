package com.example.myapplication.newHabit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.HabitType
import com.example.myapplication.repositories.database.Habit
import com.example.myapplication.repositories.inmemory.HabitsRepo
import kotlinx.android.synthetic.main.new_habit_fragment.*
import kotlinx.android.synthetic.main.new_habit_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NewHabitViewModel(private val habitId: String?) : ViewModel() {

    val habit: LiveData<Habit?> =  App.habitsRepository.getHabitById(habitId)

    fun verifyHabitParams(
        nameText: String,
        descriptionText:String,
        typeId: Int,
        quantityText: String,
        periodicityText: String
    ) :Boolean {
        if (nameText == "" ||
            descriptionText == "" ||
            typeId == -1 ||
            quantityText == "" ||
            periodicityText == "")
                return false
        return true
    }

    fun saveHabit( nameText: String,
                   descriptionText:String,
                   priority: Int,
                   typeId: HabitType,
                   quantityText: String,
                   periodicityText: String
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            App.habitsRepository.addOrUpdate(
                habitId,
                nameText,
                descriptionText,
                priority,
                typeId,
                quantityText.toInt(),
                periodicityText.toInt()
            )
        }
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        App.habitsRepository.delete(habit.value!!)
    }
}