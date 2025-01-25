package com.inf8405.expensetracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inf8405.expensetracker.R
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.ui.screens.CategoriesScreen
import com.inf8405.expensetracker.ui.screens.ChartsScreen
import com.inf8405.expensetracker.ui.screens.HomeScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
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
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.home)) },
                        selected = currentScreen == ExpenseTrackerScreen.Home,
                        onClick = {
                            scope.launch {
                                navController.navigate(ExpenseTrackerScreen.Home.name)
                                drawerState.close()
                            }
                        },
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.charts)) },
                        selected = currentScreen == ExpenseTrackerScreen.Charts,
                        onClick = {
                            scope.launch {
                                navController.navigate(ExpenseTrackerScreen.Charts.name)
                                drawerState.close()
                            }
                        },
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.categories)) },
                        selected = currentScreen == ExpenseTrackerScreen.Categories,
                        onClick = {
                            scope.launch {
                                navController.navigate(ExpenseTrackerScreen.Categories.name)
                                drawerState.close()
                            }
                        },
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(currentScreen.title)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = colorResource(R.color.green),
                        titleContentColor = Color.White
                    ),
                )
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
                composable(route = ExpenseTrackerScreen.Home.name) {
                    HomeScreen()
                }
                composable(
                    route = ExpenseTrackerScreen.Charts.name
                ) {
                    ChartsScreen()
                }
                composable(route = ExpenseTrackerScreen.Categories.name) {
                    CategoriesScreen()
                }
                // TODO: Ajouter les autres routes/pages
            }
        }
    }

}

@Preview
@Composable
fun ScreenPreview() {
    ExpenseTrackingApp()
}
