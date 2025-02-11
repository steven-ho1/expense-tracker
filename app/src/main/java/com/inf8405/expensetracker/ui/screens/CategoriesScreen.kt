package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inf8405.expensetracker.models.MainViewModelsWrapper

data class Category(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val backgroundColor: Color
)

@Composable
fun CategoriesScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
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
                text = { Text("EXPENSES") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("INCOME") }
            )
        }

        // Wrap the CategoriesGrid in a Box with weight modifier
        Box(modifier = Modifier.weight(1f)) {
            CategoriesGrid(
                categories = getSampleCategories(),
                onCategoryClick = { category ->
                    // Handle category selection
                }
            )
        }
    }
}

@Composable
fun CategoriesGrid(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize() // This is now safe because parent Box has weight
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
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.large)
                .background(category.backgroundColor)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.title,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
            )
        }

        Text(
            text = category.title,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun getSampleCategories(): List<Category> {
    return listOf(
        Category(1, "Health", Icons.Filled.Favorite, Color(0xFFE57373)),      // Red
        Category(2, "Leisure", Icons.Filled.Star, Color(0xFF81C784)),    // Mint
        Category(3, "Home", Icons.Filled.Home, Color(0xFFFFB74D)),          // Orange
        Category(4, "Cafe", Icons.Filled.Star, Color(0xFFBA68C8)),          // Purple
        Category(5, "Education", Icons.Filled.Star, Color(0xFF64B5F6)), // Blue
        Category(6, "Gifts", Icons.Filled.Star, Color(0xFFF06292)),        // Pink
        Category(7, "Groceries", Icons.Filled.ShoppingCart, Color(0xFFA5D6A7)),// Green
        Category(8, "Family", Icons.Filled.Person, Color(0xFFFFD54F)),      // Yellow
        Category(9, "Workout", Icons.Filled.Star, Color(0xFFFF8A65)),    // Orange
        Category(10, "Transportation", Icons.Filled.Star, Color(0xFF4FC3F7)), // Blue
        Category(11, "Other", Icons.Filled.MoreVert, Color(0xFF90A4AE))        // Gray
    )
}