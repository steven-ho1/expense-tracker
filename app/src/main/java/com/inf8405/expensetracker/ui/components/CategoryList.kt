package com.inf8405.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    onCategorySelect: (String) -> Unit
) {
    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            items(categories) { category ->
                Box(
                    modifier = Modifier
                        .clickable { onCategorySelect(category.name) }
                ) {
                    CategoryItem(category = category, selectedCategory == category.name)
                }
            }
            item {
                Box() {
                    IconButton(
                        onClick = { navController.navigate(ExpenseTrackerScreen.AddCategory.name) },
                        modifier = Modifier
                            .background(Color.Gray, shape = CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Category",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: CategoryEntity, isCategorySelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(category.color.toColor())
                .then(
                    if (isCategorySelected) {
                        Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                    } else {
                        Modifier
                    }
                )
        )
        Text(
            text = category.name,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

