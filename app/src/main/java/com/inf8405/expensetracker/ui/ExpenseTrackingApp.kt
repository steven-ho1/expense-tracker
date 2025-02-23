package com.inf8405.expensetracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.ui.components.AppBar
import com.inf8405.expensetracker.ui.components.DrawerContent
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.ui.navigation.expenseTrackerNavGraph
import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import com.inf8405.expensetracker.viewmodels.TransactionViewModel

@Composable
fun ExpenseTrackingApp(
) {
    val navController: NavHostController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen: ExpenseTrackerScreen = ExpenseTrackerScreen.valueOf(
        backStackEntry?.destination?.route ?: ExpenseTrackerScreen.Home.name
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val transactionViewModel: TransactionViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val mainViewModelsWrapper = MainViewModelsWrapper(transactionViewModel, categoryViewModel)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(currentScreen, navController, drawerState, scope) }
    ) {
        Scaffold(
            topBar = {
                AppBar(currentScreen, drawerState, scope, navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ExpenseTrackerScreen.Home.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                expenseTrackerNavGraph(navController, mainViewModelsWrapper)
            }
        }
    }

}
