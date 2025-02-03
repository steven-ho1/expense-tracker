package com.inf8405.expensetracker.database.repositories

import com.inf8405.expensetracker.database.dao.TransactionDao
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.TransactionType

// Intermédiaire entre le DAO (qui interagit avec la BD) et un ViewModel (logique d'UI)
class TransactionRepository(private val transactionDao: TransactionDao) {
    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    suspend fun getTransactionsByType(type: TransactionType): List<TransactionEntity> {
        return transactionDao.getTransactionsByType(type)
    }

    suspend fun getBalance(): Double {
        val incomeTransactions = transactionDao.getTransactionsByType(TransactionType.INCOME)
        val expenseTransactions = transactionDao.getTransactionsByType(TransactionType.EXPENSES)

        val totalIncome = incomeTransactions.sumOf { it.amount }
        val totalExpenses = expenseTransactions.sumOf { it.amount }

        return totalIncome - totalExpenses
    }
}