package com.example.myapplication

import android.app.Application
import androidx.room.Room
import com.example.myapplication.repositories.database.HabitsDB

class App : Application() {
    companion object {
        lateinit var database: HabitsDB
    }

    override fun onCreate() {
        super.onCreate()

        database = HabitsDB.getInstance(applicationContext)
    }
}