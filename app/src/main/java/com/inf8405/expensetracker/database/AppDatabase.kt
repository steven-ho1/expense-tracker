package com.inf8405.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inf8405.expensetracker.database.converters.TransactionConverters
import com.inf8405.expensetracker.database.dao.CategoryDao
import com.inf8405.expensetracker.database.dao.TransactionDao
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.database.entities.TransactionEntity

/*
    Sources utiles:
    https://developer.android.com/training/data-storage/room#kotlin
    https://medium.com/@stephenmuindi241/singleton-pattern-in-room-database-566c250196aa
*/
@Database(
    entities = [TransactionEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransactionConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    // Une seule instance de la DB (Singleton)
    companion object {
        private var _instance: AppDatabase? = null

        val instance: AppDatabase
            get() {
                return _instance
                    ?: throw IllegalStateException("Database not initialized. Call init() first.")
            }

        fun init(context: Context): AppDatabase {
            if (_instance == null) {
                _instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_tracker_database"
                ).build()
            }
            return _instance!!
        }
    }
}