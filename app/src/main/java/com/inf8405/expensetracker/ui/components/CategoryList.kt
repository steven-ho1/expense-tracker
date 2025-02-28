package com.inf8405.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.utils.toColor

@Composable
fun CategoryList(
    navController: NavController,
    categories: List<CategoryEntity>,
    selectedCategory: String?,
    modifier: Modifier = Modifier,
    onCategorySelect: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .height(200.dp)
            .padding(horizontal = 25.dp),
    ) {
        items(categories) { category ->
            Box(
                modifier.clickable { onCategorySelect(category.name) },
                contentAlignment = Alignment.Center
            ) {
                CategoryItem(category = category, selectedCategory == category.name)
            }
        }
    }

    IconButton(
        onClick = { navController.navigate(ExpenseTrackerScreen.AddCategory.name) },
        modifier = modifier
            .background(Color.Gray, shape = CircleShape)
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Ajouter une catégorie",
            tint = Color.White
        )
    }
}

@Composable
fun CategoryItem(
    category: CategoryEntity,
    isCategorySelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Box(
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(category.color.toColor())
                .then(
                    if (isCategorySelected) {
                        modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                    } else {
                        modifier
                    }
                )
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(top = 8.dp)
        )
    }
}

