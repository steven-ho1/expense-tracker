package com.inf8405.expensetracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inf8405.expensetracker.ui.components.AppBar
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.ui.screens.MainScreen


@Composable
fun ExpenseTrackingApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen: ExpenseTrackerScreen = ExpenseTrackerScreen.valueOf(
        backStackEntry?.destination?.route ?: ExpenseTrackerScreen.Main.name
    )

    Scaffold(
        topBar = {
            AppBar(
                currentScreen = currentScreen,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ExpenseTrackerScreen.Main.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = ExpenseTrackerScreen.Main.name) {
                MainScreen()
            }
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    ExpenseTrackingApp()
}
