package com.example.myapplication.network

import android.util.JsonToken
import com.example.myapplication.repositories.database.Habit
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface HabitOperationsAPI {
    @GET("habit")
    suspend fun getHabits(): Response<List<Habit>>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit): Response<HabitUID>

    @HTTP(method="DELETE", path="habit", hasBody=true)
    suspend fun deleteHabit(@Body habitUID: HabitUID): Response<Unit>
}