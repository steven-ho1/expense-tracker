package com.inf8405.expensetracker.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inf8405.expensetracker.ui.screens.CategoriesScreen
import com.inf8405.expensetracker.ui.screens.ChartsScreen
import com.inf8405.expensetracker.ui.screens.HomeScreen
import com.inf8405.expensetracker.ui.screens.NewTransactionScreen

fun NavGraphBuilder.expenseTrackerNavGraph(navController: NavController) {
    composable(route = ExpenseTrackerScreen.Home.name) {
        HomeScreen(navController)
    }
    composable(route = ExpenseTrackerScreen.Categories.name) {
        CategoriesScreen()
    }
    composable(route = ExpenseTrackerScreen.Charts.name) {
        ChartsScreen()
    }
    composable(route = ExpenseTrackerScreen.NewTransaction.name) {
        NewTransactionScreen()
    }
    // TODO: Ajouter les autres routes/pages
}