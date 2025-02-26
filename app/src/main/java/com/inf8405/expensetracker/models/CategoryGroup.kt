package com.inf8405.expensetracker.models

data class TransactionGroup (
    var categoryName: String = "",
    var categoryTotalAmount: Double = 0.0,
    var categoryColor: String = "#000000",
    var contributionPercentage: Double = 0.0
)
