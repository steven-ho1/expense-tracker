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

// Data class pour stocker les transactions d'une période spécifique
data class TransactionDetailsData(
    val periodRange: String,
    val transactions: List<TransactionEntity>
)

// Data class pour contenir les données d'un graphique en barres
data class BarChartData(
    val entries: List<BarEntry>,
    val labels: List<String>
)

private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)

class ChartsRepository(private val transactionDao: TransactionDao) {
    /**
     * Convertit une date sous forme de chaîne en LocalDate.
     * Input: String (format "dd-MM-yyyy")
     * Output: LocalDate
     */
    private fun parseTransactionDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }
    /**
     * Récupère les transactions d'un type spécifique depuis la base de données.
     * Input: TransactionType
     * Output: Liste de TransactionEntity
     */
    private suspend fun getTransactionsFromDb(type: TransactionType): List<TransactionEntity> {
        return transactionDao.getTransactionsByType(type)
    }

    /**
     * Génère les données pour un graphique en barres en fonction d'une période et d'un type de transaction.
     * Input: période (Journalier, Hebdomadaire, Mensuel, Annuel), type de transaction
     * Output: Flow contenant BarChartData (données et labels pour le graphique)
     */
    fun getTransactionsByPeriod(period: String, type: TransactionType): Flow<BarChartData> = flow {
        val today = LocalDate.now()
        val startDates = when (period) {
            "Journalier"    -> (0..5).map { today.minusDays(it.toLong()) }
            "Hebdomadaire"  -> (0..5).map { today.minusWeeks(it.toLong()) }
            "Mensuel"       -> (0..5).map { today.minusMonths(it.toLong()) }
            "Annuel"        -> (0..5).map { today.minusYears(it.toLong()) }
            else            -> emptyList()
        }.reversed()
    
        val transactions = getTransactionsFromDb(type)
        val groupedData = startDates.mapIndexed { index, date ->
            val total = transactions
                .filter {
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
                .sumOf { it.amount }
            BarEntry(index.toFloat(), total.toFloat())
        }
    
        val outputFormatter = when (period) {
            "Mensuel" -> DateTimeFormatter.ofPattern("MM-yyyy")
            "Annuel"  -> DateTimeFormatter.ofPattern("yyyy")
            else      -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        }
        val labels = startDates.map { it.format(outputFormatter) }
    
        emit(BarChartData(entries = groupedData, labels = labels))
    }
    
    /**
     * Récupère les détails des transactions associées à une barre spécifique du graphique.
     * Input: période, type de transaction, index de la barre sélectionnée
     * Output: Flow contenant TransactionDetailsData (détails des transactions pour la période sélectionnée)
     */
    fun getTransactionDetailsByBar(
        period: String,
        type: TransactionType,
        barIndex: Int
    ): Flow<TransactionDetailsData> = flow {
        val today = LocalDate.now()
        val startDates = when (period) {
            "Journalier"    -> (0..5).map { today.minusDays(it.toLong()) }
            "Hebdomadaire"  -> (0..5).map { today.minusWeeks(it.toLong()) }
            "Mensuel"       -> (0..5).map { today.minusMonths(it.toLong()) }
            "Annuel"        -> (0..5).map { today.minusYears(it.toLong()) }
            else            -> emptyList()
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
            "Journalier" -> "${selectedDate.format(outputFormatter)}"
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
