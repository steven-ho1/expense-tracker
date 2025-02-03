package com.inf8405.expensetracker.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inf8405.expensetracker.models.MainViewModelsWrapper

@Composable
fun CategoriesScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    modifier: Modifier = Modifier) {
    Text(text = "Page des catégories")
}