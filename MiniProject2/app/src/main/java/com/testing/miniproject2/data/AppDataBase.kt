package com.testing.miniproject2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Tarea::class], version = 2) // antes estaba en 1
abstract class AppDatabase : RoomDatabase() {
    abstract fun tareaDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tu_nombre_db"
                )
                    .fallbackToDestructiveMigration() // agrega esta línea
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}