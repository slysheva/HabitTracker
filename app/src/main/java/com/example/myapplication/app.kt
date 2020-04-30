package com.example.myapplication

import android.app.Application
import com.example.myapplication.network.AuthInterceptor
import com.example.myapplication.network.HabitOperationsAPI
import com.example.myapplication.network.HabitTypeTypeAdapter
import com.example.myapplication.repositories.database.HabitsDB
import com.example.myapplication.repositories.database.HabitsRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {
        lateinit var database: HabitsDB
        lateinit var habitsRepository: HabitsRepository
        private const val SERVER_URL = "https://droid-test-server.doubletapp.ru/api/"
        private const val SERVER_TIMEOUT_SEC = 5L
    }

    override fun onCreate() {
        super.onCreate()

        database = HabitsDB.getInstance(applicationContext)

        val gson = GsonBuilder()
            .registerTypeAdapter(HabitType::class.java, HabitTypeTypeAdapter())
            .create()

        val okHttpClient= OkHttpClient().newBuilder()
            .addInterceptor(AuthInterceptor())
            .writeTimeout(SERVER_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(SERVER_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(SERVER_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val habitApi = retrofit.create(HabitOperationsAPI::class.java)

        habitsRepository = HabitsRepository(database, habitApi)
    }
}