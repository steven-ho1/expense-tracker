package com.inf8405.expensetracker.ui.navigation

import androidx.annotation.StringRes
import com.inf8405.expensetracker.R

enum class ExpenseTrackerScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Categories(title = R.string.categories),
    Charts(title = R.string.charts),
    NewTransaction(title = R.string.add_transaction),
    AddCategory(title = R.string.add_category)
}