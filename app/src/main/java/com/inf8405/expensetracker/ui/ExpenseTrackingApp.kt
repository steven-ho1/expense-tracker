package com.inf8405.expensetracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inf8405.expensetracker.ui.components.AppBar
import com.inf8405.expensetracker.ui.components.DrawerContent
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.ui.navigation.expenseTrackerNavGraph


@Composable
fun ExpenseTrackingApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen: ExpenseTrackerScreen = ExpenseTrackerScreen.valueOf(
        backStackEntry?.destination?.route ?: ExpenseTrackerScreen.Home.name
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                expenseTrackerNavGraph(navController)
            }
        }
    }

}

@Preview
@Composable
fun ScreenPreview() {
    ExpenseTrackingApp()
}
