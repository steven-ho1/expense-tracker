package com.inf8405.expensetracker.ui.navigation

import androidx.annotation.StringRes
import com.inf8405.expensetracker.R

enum class ExpenseTrackerScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Categories(title = R.string.categories),
    Charts(title = R.string.charts),
    // TODO: Ajouter les autres routes/pages
}