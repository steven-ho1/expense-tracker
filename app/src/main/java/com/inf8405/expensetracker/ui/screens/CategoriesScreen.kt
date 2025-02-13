package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale


@Composable
fun CategoriesScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Page des catégories",
            modifier = Modifier.align(Alignment.Center)
        )

        FloatingActionButton(
            onClick = {
                navController.navigate(ExpenseTrackerScreen.AddCategory.name)
            },
            modifier = Modifier
                .scale(1f)
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}