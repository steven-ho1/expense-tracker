package com.inf8405.expensetracker.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inf8405.expensetracker.models.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: String
)

