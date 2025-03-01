package com.inf8405.expensetracker.database.repositories

import com.github.mikephil.charting.data.BarEntry
import com.inf8405.expensetracker.database.dao.TransactionDao
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import formatUtcMillisToString
import com.inf8405.expensetracker.models.DefaultCategories


data class BarChartData(
    val entries: List<BarEntry>,
    val labels: List<String>,
    val stackLabels: List<String> // Ajouté pour la légende du stack
)


data class TransactionDetailsData(
    val periodRange: String,
    val transactions: List<TransactionEntity>
)

class ChartsRepository(
    private val transactionDao: TransactionDao,
    private val categoryRepository: CategoryRepository // Injection du CategoryRepository
) {
    private fun parseTransactionDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
        return LocalDate.parse(dateString, formatter)
    }

    // Récupère les transactions en fonction du type
    private suspend fun getTransactionsFromDb(type: TransactionType): List<TransactionEntity> {
        return transactionDao.getTransactionsByType(type)
    }

    // Création d'un stacked bar chart
    fun getStackedTransactionsByPeriod(period: String, type: TransactionType): Flow<BarChartData> = flow {
        val today = LocalDate.now()
        val startDates = when (period) {
            "Journalier"   -> (0..5).map { today.minusDays(it.toLong()) }
            "Hebdomadaire" -> (0..5).map { today.minusWeeks(it.toLong()) }
            "Mensuel"      -> (0..5).map { today.minusMonths(it.toLong()) }
            "Annuel"       -> (0..5).map { today.minusYears(it.toLong()) }
            else           -> emptyList()
        }.reversed()
    
        val transactions = getTransactionsFromDb(type)
        // Récupération des catégories combinées
        val dbCategories = categoryRepository.getCategories()
        val defaultCategories = when (type) {
            TransactionType.EXPENSES -> DefaultCategories.expenseCategories
            TransactionType.INCOME -> DefaultCategories.incomeCategories
        }
        // Fusionner en éliminant les doublons par nom (pour garantir l'ordre)
        val combinedCategories = (defaultCategories + dbCategories.filter { it.type == type })
            .distinctBy { it.name }
        val categoryNames = if (combinedCategories.isEmpty()) {
            listOf("Default")
        } else {
            combinedCategories.map { it.name }
        }
    
        val entries = startDates.mapIndexed { index, date ->
            val filteredTx = transactions.filter {
                when (period) {
                    "Journalier" -> parseTransactionDate(formatUtcMillisToString(it.date)).isEqual(date)
                    "Hebdomadaire" -> {
                        val txDate = parseTransactionDate(formatUtcMillisToString(it.date))
                        txDate >= date.minusDays(6) && txDate <= date
                    }
                    "Mensuel" -> {
                        val txDate = parseTransactionDate(formatUtcMillisToString(it.date))
                        txDate.month == date.month && txDate.year == date.year
                    }
                    "Annuel" -> {
                        val txDate = parseTransactionDate(formatUtcMillisToString(it.date))
                        txDate.year == date.year
                    }
                    else -> false
                }
            }
            val stackValues = FloatArray(categoryNames.size) { 0f }
            filteredTx.forEach { transaction ->
                val catIndex = categoryNames.indexOfFirst { 
                    it.trim().equals(transaction.category.trim(), ignoreCase = true) 
                }
                if (catIndex != -1) {
                    stackValues[catIndex] += transaction.amount.toFloat()
                }
            }
            println("Date: $date, transactions filtrées: ${filteredTx.size}, stackValues: ${stackValues.joinToString()}")
            BarEntry(index.toFloat(), stackValues)
        }
    
        val outputFormatter = when (period) {
            "Mensuel" -> DateTimeFormatter.ofPattern("MM-yyyy")
            "Annuel"  -> DateTimeFormatter.ofPattern("yyyy")
            else      -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        }
        val labels = startDates.map { it.format(outputFormatter) }
        emit(BarChartData(entries = entries, labels = labels, stackLabels = categoryNames))
    }
    

    fun getTransactionDetailsByBar(
        period: String,
        type: TransactionType,
        barIndex: Int
    ): Flow<TransactionDetailsData> = flow {
        val today = LocalDate.now()
        val startDates = when (period) {
            "Journalier"   -> (0..5).map { today.minusDays(it.toLong()) }
            "Hebdomadaire" -> (0..5).map { today.minusWeeks(it.toLong()) }
            "Mensuel"      -> (0..5).map { today.minusMonths(it.toLong()) }
            "Annuel"       -> (0..5).map { today.minusYears(it.toLong()) }
            else           -> emptyList()
        }.reversed()

        if (barIndex < 0 || barIndex >= startDates.size) {
            emit(TransactionDetailsData("", emptyList()))
            return@flow
        }

        val selectedDate = startDates[barIndex]
        val transactions = getTransactionsFromDb(type)
        val details = when (period) {
            "Journalier" -> transactions.filter {
                parseTransactionDate(formatUtcMillisToString(it.date)).isEqual(selectedDate)
            }
            "Hebdomadaire" -> transactions.filter {
                val txDate = parseTransactionDate(formatUtcMillisToString(it.date))
                txDate >= selectedDate.minusDays(6) && txDate <= selectedDate
            }
            "Mensuel" -> transactions.filter {
                val txDate = parseTransactionDate(formatUtcMillisToString(it.date))
                txDate.month == selectedDate.month && txDate.year == selectedDate.year
            }
            "Annuel" -> transactions.filter {
                val txDate = parseTransactionDate(formatUtcMillisToString(it.date))
                txDate.year == selectedDate.year
            }
            else -> emptyList()
        }

        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val periodRange = when (period) {
            "Journalier" -> selectedDate.format(outputFormatter)
            "Hebdomadaire" -> {
                val startPeriod = selectedDate.minusDays(6)
                "${startPeriod.format(outputFormatter)} à ${selectedDate.format(outputFormatter)}"
            }
            "Mensuel" -> {
                val startPeriod = selectedDate.withDayOfMonth(1)
                val endPeriod = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth())
                "${startPeriod.format(outputFormatter)} à ${endPeriod.format(outputFormatter)}"
            }
            "Annuel" -> {
                val startPeriod = selectedDate.withDayOfYear(1)
                val endPeriod = selectedDate.withDayOfYear(selectedDate.lengthOfYear())
                "${startPeriod.format(outputFormatter)} à ${endPeriod.format(outputFormatter)}"
            }
            else -> ""
        }

        emit(TransactionDetailsData(periodRange, details))
    }
}
