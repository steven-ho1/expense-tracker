package com.inf8405.expensetracker.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.ui.screens.CategoriesScreen
import com.inf8405.expensetracker.ui.screens.HomeScreen
import com.inf8405.expensetracker.ui.screens.NewTransactionScreen


fun NavGraphBuilder.expenseTrackerNavGraph(
    navController: NavController,
    mainViewModelsWrapper: MainViewModelsWrapper
) {
    composable(route = ExpenseTrackerScreen.Home.name) {
        HomeScreen(mainViewModelsWrapper, navController)
    }

    composable(route = ExpenseTrackerScreen.NewTransaction.name) {
        NewTransactionScreen(mainViewModelsWrapper, navController)
    }

    composable(route = ExpenseTrackerScreen.Categories.name) {
        CategoriesScreen(mainViewModelsWrapper)
    }
//    composable(route = ExpenseTrackerScreen.Charts.name) {
//        ChartsScreen()
//    }
//    composable(route = ExpenseTrackerScreen.Charts.name) {
//        val chartsViewModel: ChartsViewModel = viewModel()
//        ChartsScreen(chartsViewModel)
//    }
    // TODO: Ajouter les autres routes/pages
}