package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen

@Composable
fun CategoriesScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
) {
    var selectedTab by remember { mutableStateOf(0) }
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel
    val expenseCategories by categoryViewModel.expenseCategories.collectAsState()
    val incomeCategories by categoryViewModel.incomeCategories.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2D4F3C))
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF2D4F3C),
                contentColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Dépenses") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Revenus") }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                CategoriesGrid(
                    categories = if (selectedTab == 0) expenseCategories else incomeCategories,
                    onCategoryClick = { category ->
                        // Handle category selection
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(ExpenseTrackerScreen.AddCategory.name)
            },
            modifier = Modifier
                .scale(1f)
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun CategoriesGrid(
    categories: List<CategoryEntity>,
    onCategoryClick: (CategoryEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryEntity,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(category.color)))
        )

        Text(
            text = category.name,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}