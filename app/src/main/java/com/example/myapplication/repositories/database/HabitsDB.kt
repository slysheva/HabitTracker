package com.example.myapplication.repositories.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class HabitsDB : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {
        private var DB_INSTANCE: HabitsDB? = null

        fun getInstance(context: Context) = DB_INSTANCE ?:
            DB_INSTANCE ?: buildDatabase(context).also { DB_INSTANCE = it }


        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            HabitsDB::class.java,
            "habits.db"
        )
            .allowMainThreadQueries()
            .build()
    }
}