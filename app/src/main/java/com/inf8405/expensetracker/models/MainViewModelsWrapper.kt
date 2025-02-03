package com.inf8405.expensetracker.models

import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import com.inf8405.expensetracker.viewmodels.TransactionViewModel

data class MainViewModelsWrapper(
    val transactionViewModel: TransactionViewModel,
    val categoryViewModel: CategoryViewModel
)
