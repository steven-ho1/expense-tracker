
package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.viewmodels.AddCategoryViewModel


enum class TransactionType {
    EXPENSE,
    INCOME
}


data class IconData(
    val icon: ImageVector,
    val description: String
)

@Composable
fun AddCategoryScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
    addCategoryViewModel: AddCategoryViewModel = viewModel()  // obtain the ViewModel
) {

    var categoryName by remember { mutableStateOf("") }
    var selectedTransactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var monthlyLimit by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<IconData?>(null) }

    val icons = listOf(
        IconData(Icons.Default.Menu, "Menu"),
        IconData(Icons.Default.Home, "Home"),
        IconData(Icons.Default.Star, "Star"),
        IconData(Icons.Default.Favorite, "Favorite"),
        IconData(Icons.Default.Face, "Face"),
        IconData(Icons.Default.Email, "Email"),
        IconData(Icons.Default.Phone, "Phone"),
        IconData(Icons.Default.Place, "Place"),
        IconData(Icons.Default.Build, "Build"),
        IconData(Icons.Default.ShoppingCart, "Shopping Cart"),
        IconData(Icons.Default.Settings, "Settings"),
        IconData(Icons.Default.Info, "Info"),
        IconData(Icons.Default.Lock, "Lock"),
        IconData(Icons.Default.Search, "Search")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Category Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Category Type")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            RadioButton(
                selected = (selectedTransactionType == TransactionType.EXPENSE),
                onClick = { selectedTransactionType = TransactionType.EXPENSE }
            )
            Text(
                text = "Expense",
                modifier = Modifier
                    .clickable { selectedTransactionType = TransactionType.EXPENSE }
                    .padding(end = 16.dp)
            )
            RadioButton(
                selected = (selectedTransactionType == TransactionType.INCOME),
                onClick = { selectedTransactionType = TransactionType.INCOME }
            )
            Text(
                text = "Income",
                modifier = Modifier
                    .clickable { selectedTransactionType = TransactionType.INCOME }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = monthlyLimit,
            onValueChange = { monthlyLimit = it },
            label = { Text("Monthly Limit (CAD)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Select an Icon")
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .height(200.dp)
                .padding(8.dp)
        ) {
            items(icons) { iconData ->
                Icon(
                    imageVector = iconData.icon,
                    contentDescription = iconData.description,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clickable { selectedIcon = iconData }
                        .background(
                            color = if (selectedIcon == iconData) Color.LightGray else Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val limit = monthlyLimit.toDoubleOrNull() ?: 0.0


                addCategoryViewModel.addCategory(categoryName, selectedTransactionType, limit, selectedIcon)

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Category")
        }
    }
}
