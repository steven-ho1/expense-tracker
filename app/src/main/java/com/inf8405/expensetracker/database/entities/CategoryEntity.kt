package com.inf8405.expensetracker.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inf8405.expensetracker.models.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: TransactionType,
    val color: String
)