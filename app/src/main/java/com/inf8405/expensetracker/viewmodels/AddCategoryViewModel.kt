// File: AddCategoryViewModel.kt
package com.inf8405.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.inf8405.expensetracker.ui.screens.IconData

class AddCategoryViewModel : ViewModel() {
    fun addCategory(
        categoryName: String,
        transactionType: com.inf8405.expensetracker.ui.screens.TransactionType,
        monthlyLimit: Double,
        icon: IconData?
    ) {

    }
}
