package com.inf8405.expensetracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.R
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    currentScreen: ExpenseTrackerScreen,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
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
                label = { Text(stringResource(R.string.categories)) },
                selected = currentScreen == ExpenseTrackerScreen.Categories,
                onClick = {
                    scope.launch {
                        navController.navigate(ExpenseTrackerScreen.Categories.name)
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
        }
    }
}