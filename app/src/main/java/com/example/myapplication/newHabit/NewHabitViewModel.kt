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

class NewHabitViewModel(private val habitId: Int) : ViewModel() {
    private val mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> =  App.database.habitDao().getById(habitId)

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
        val habit = Habit (
            if (habitId == -1) null else habitId,
            nameText,
            descriptionText,
            priority,
            typeId,
            quantityText.toInt(),
            periodicityText.toInt(),
            Date()
        )
        viewModelScope.launch(Dispatchers.IO) {
            val habitDao = App.database.habitDao()
            if (habitId == -1) {
                habitDao.insert(habit)
            } else {
                habit.id = habitId
                habitDao.update(habit)
            }
        }
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        val habitDao = App.database.habitDao()
            habitDao.delete(habit.value!!)
    }
}