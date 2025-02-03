package com.inf8405.expensetracker.database.converters

import androidx.room.TypeConverter
import com.inf8405.expensetracker.models.TransactionType

// Convertir les données en des types acceptés par Room
class TransactionConverters {
    // Convertir l'enum TransactionType en String pour la sauvegarde dans la BD
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    // Convertir String en TransactionType lors de la lecture de la BD
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}
