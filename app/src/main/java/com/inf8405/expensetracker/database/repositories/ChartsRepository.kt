package com.inf8405.expensetracker.database.repositories

import com.github.mikephil.charting.data.BarEntry
import com.inf8405.expensetracker.database.dao.TransactionDao
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChartsRepository(private val transactionDao: TransactionDao) {

    private fun getStartDate(period: String): String {
        val today = LocalDate.now()
        return when (period) {
            "Journalier" -> today.minusDays(6)
            "Hebdomadaire" -> today.minusWeeks(6)
            "Mensuel" -> today.minusMonths(6)
            "Annuel" -> today.minusYears(6)
            else -> today.minusWeeks(6)
        }.toString() // Convertir en String formaté
    }

    fun getTransactionsByPeriod(period: String, type: TransactionType): Flow<List<BarEntry>> {
        return flow {
            val transactions: List<TransactionEntity> =
                transactionDao.getTransactionsByPeriod(type, getStartDate(period))
            val entries = transactions.mapIndexed { index, transaction ->
                BarEntry(index.toFloat(), transaction.amount.toFloat())
            }
            emit(entries) // Émettre la liste de BarEntry
        }
    }
}
