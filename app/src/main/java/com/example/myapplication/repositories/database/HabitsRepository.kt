package com.example.myapplication.repositories.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myapplication.HabitType
import com.example.myapplication.network.HabitOperationsAPI
import com.example.myapplication.network.HabitUID
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import java.util.*

class HabitsRepository(private val habitsDataSource: HabitsDB, private val habitApi: HabitOperationsAPI) {

    companion object {
        private const val RETRY_TIMEOUT = 500L
    }

    fun getAllHabits(): LiveData<List<Habit>> = habitsDataSource.habitDao().getAll()

    fun getHabitById(uid: String?): LiveData<Habit?> = habitsDataSource.habitDao().getById(uid)

    suspend fun initializeHabits() {
        makeRequestWithRetries(
            { habitApi.getHabits() },
            { serverHabitsList ->
                Log.d("tag", "in initialize")
                for (serverHabit in serverHabitsList) {
                    val localHabit = habitsDataSource.habitDao().getById(serverHabit.id!!).value
                    if (localHabit == null) {
                        withContext(Dispatchers.IO) {
                            habitsDataSource.habitDao().insert(serverHabit)
                        }
                    }
                    else {
                        if (localHabit.creationDate < serverHabit.creationDate) {
                            withContext(Dispatchers.IO) {
                                habitsDataSource.habitDao().update(serverHabit)
                            }
                        }
                    }
                }
            }
        )
    }

    suspend fun addOrUpdate(
        habitId: String?,
        nameText: String,
        descriptionText:String,
        priority: Int,
        typeId: HabitType,
        quantity: Int,
        periodicity: Int) {
        val habit = Habit(
            nameText,
            descriptionText,
            priority,
            typeId,
            quantity,
            periodicity,
            Date().time
        )
        makeRequestWithRetries(
            {
                habit.id = habitId
                val str = Gson().toJson(habit)
                Log.d("tag", "creating $str")
                withContext(Dispatchers.IO) {
                    habitApi.addOrUpdateHabit(habit)
                }
            },
            {
                Log.d("tag", "in local creating")

                habit.id = it.uid
                if (habitId == null) {
                    withContext(Dispatchers.IO) {
                        habitsDataSource.habitDao().insert(habit)
                    }
                }
                else {
                    withContext(Dispatchers.IO) {
                        habitsDataSource.habitDao().update(habit)
                    }
                }
            }
        )
    }

    suspend fun delete(habit: Habit) {
        makeRequestWithRetries(
            { habitApi.deleteHabit(HabitUID(habit.id)) },
            { withContext(Dispatchers.IO) { habitsDataSource.habitDao().delete(habit) } }
        )
    }

    private suspend fun <T> makeRequestWithRetries(
        serverFunc: suspend () -> Response<T>,
        localFunc: suspend (T) -> Unit
    ) {
        while (true) {
            try {
                val response = serverFunc()
                if (response.isSuccessful) {
                    Log.d("tag", "success")
                    localFunc(response.body()!!)
                    break
                }
                else {
                    Log.d("tag", response.message())
                    if (response.code() >= 500)
                        Thread.sleep(RETRY_TIMEOUT)
                    else
                        break
                }
            }
            catch (e: Exception) {
                Log.e("tag", e.toString())
                break
            }
        }
    }
}
