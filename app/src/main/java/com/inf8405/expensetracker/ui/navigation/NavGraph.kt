package com.inf8405.expensetracker.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inf8405.expensetracker.ui.screens.CategoriesScreen
import com.inf8405.expensetracker.ui.screens.ChartsScreen
import com.inf8405.expensetracker.ui.screens.HomeScreen

fun NavGraphBuilder.expenseTrackerNavGraph() {
    composable(route = ExpenseTrackerScreen.Home.name) {
        HomeScreen()
    }
    composable(route = ExpenseTrackerScreen.Categories.name) {
        CategoriesScreen()
    }
    composable(route = ExpenseTrackerScreen.Charts.name) {
        ChartsScreen()
    }
    // TODO: Ajouter les autres routes/pages

}