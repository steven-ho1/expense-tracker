package com.inf8405.expensetracker.utils

import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.TransactionGroup

fun groupByCategory(
    transactions: List<TransactionEntity>,
    categories: List<CategoryEntity>
): List<TransactionGroup> {
    val transactionGroups = transactions
        .groupBy { it.category }
        .map { (categoryName, transactions) ->
            val categoryTotalAmount = transactions.sumOf { it.amount }
            val categoryColor = categories.find { it.name == categoryName }?.color ?: "#000000"

            TransactionGroup(categoryName, categoryTotalAmount, categoryColor)
        }
    val totalAmount = transactionGroups.sumOf { it.categoryTotalAmount }

    transactionGroups.forEach {
        it.contributionPercentage = it.categoryTotalAmount / totalAmount * 100
    }

    return transactionGroups
}