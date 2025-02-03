package com.inf8405.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.database.repositories.TransactionRepository
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {
    private val transactionRepository: TransactionRepository =
        TransactionRepository(AppDatabase.instance.transactionDao())
    private val _expenses = MutableStateFlow<List<TransactionEntity>>(emptyList())
    private val _income = MutableStateFlow<List<TransactionEntity>>(emptyList())
    private val _balance = MutableStateFlow(0.0)

    /*
        Pour l'encapsulation
        Propriétés StateFlow immutables publiques
        (peuvent être observées de l'extérieur mais ne peuvent pas être modifiées)
    */
    val expenses: StateFlow<List<TransactionEntity>> = _expenses
    val income: StateFlow<List<TransactionEntity>> = _income
    val balance: StateFlow<Double> = _balance

    init {
        loadTransactions()
        loadBalance()
    }

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.insertTransaction(transaction)
            loadTransactions()
            loadBalance()
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _expenses.value = transactionRepository.getTransactionsByType(TransactionType.EXPENSES)
            _income.value = transactionRepository.getTransactionsByType(TransactionType.INCOME)
        }
    }

    private fun loadBalance() {
        viewModelScope.launch {
            _balance.value = transactionRepository.getBalance()
        }
    }
}

