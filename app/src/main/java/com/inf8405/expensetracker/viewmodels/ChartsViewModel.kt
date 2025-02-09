package com.inf8405.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

class ChartsViewModel : ViewModel() {
    private val transactionDao = AppDatabase.instance.transactionDao()

    private val _selectedType = MutableStateFlow(TransactionType.EXPENSES)  // Dépenses par défaut
    private val _selectedPeriod = MutableStateFlow("Hebdomadaire") // Période par défaut
    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())

    val selectedType: StateFlow<TransactionType> = _selectedType
    val selectedPeriod: StateFlow<String> = _selectedPeriod
    val transactions: StateFlow<List<TransactionEntity>> = _transactions.asStateFlow()

    init {
        loadTransactions()
    }

    // 📌 Met à jour le type de transactions (Dépenses ou Revenus)
    fun setTransactionType(type: TransactionType) {
        _selectedType.value = type
        loadTransactions()
    }

    // 📌 Met à jour la période sélectionnée et recharge les données
    fun setPeriod(period: String) {
        _selectedPeriod.value = period
        loadTransactions()
    }

    // 📌 Charge les transactions filtrées par type et période
    private fun loadTransactions() {
        viewModelScope.launch {
            val allTransactions = transactionDao.getTransactionsByType(_selectedType.value)
            val filteredTransactions = filterTransactionsByPeriod(allTransactions, _selectedPeriod.value)
            _transactions.value = filteredTransactions
        }
    }

    // 📌 Filtrage des transactions en fonction de la période
    private fun filterTransactionsByPeriod(transactions: List<TransactionEntity>, period: String): List<TransactionEntity> {
        val today = LocalDate.now()
        val startDate = when (period) {
            "Journalier" -> today.minusDays(6)
            "Hebdomadaire" -> today.minusWeeks(6)
            "Mensuel" -> today.minusMonths(6)
            "Annuel" -> today.minusYears(6)
            else -> today
        }

        return transactions.filter {
            LocalDate.parse(it.date, formatter).isAfter(startDate)
        }
    }
}
