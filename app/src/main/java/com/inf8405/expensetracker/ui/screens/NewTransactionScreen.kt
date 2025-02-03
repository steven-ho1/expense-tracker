package com.inf8405.expensetracker.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.viewmodels.CategoryViewModel

@Composable
fun NewTransactionScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    modifier: Modifier = Modifier
) {
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel;
}