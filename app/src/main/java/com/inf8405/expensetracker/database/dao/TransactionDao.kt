package com.inf8405.expensetracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.TransactionType

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    // Source utile: https://developer.android.com/reference/androidx/room/Query
    @Query("SELECT * FROM transactions WHERE type = :type")
    suspend fun getTransactionsByType(type: TransactionType): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE type = :type AND date >= :startDate ORDER BY date DESC")
    suspend fun getTransactionsByPeriod(type: TransactionType, startDate: String): List<TransactionEntity>

}