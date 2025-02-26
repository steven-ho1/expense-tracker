package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.R
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import androidx.compose.runtime.saveable.Saver


@Composable
fun AddCategoryScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
) {
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel
    val ColorNullableSaver: Saver<Color?, Long> = Saver(
        save = { color -> color?.value?.toLong() ?: -1L },
        restore = { value -> if (value == -1L) null else Color(value.toULong()) }
    )
    var categoryName by rememberSaveable { mutableStateOf("") }
    var selectedTransactionType by rememberSaveable { mutableStateOf(TransactionType.EXPENSES) }
    var selectedColor by rememberSaveable(stateSaver = ColorNullableSaver) { mutableStateOf<Color?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf("") }


    val colorOptions = listOf(
        colorResource(id = R.color.color_option1),
        colorResource(id = R.color.color_option2),
        colorResource(id = R.color.color_option3),
        colorResource(id = R.color.color_option4),
        colorResource(id = R.color.color_option5),
        colorResource(id = R.color.color_option6),
        colorResource(id = R.color.color_option7)
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
                selected = (selectedTransactionType == TransactionType.EXPENSES),
                onClick = { selectedTransactionType = TransactionType.EXPENSES }
            )
            Text(
                text = "Expense",
                modifier = Modifier
                    .clickable { selectedTransactionType = TransactionType.EXPENSES }
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

        Text(text = "Select a Color")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(colorOptions) { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { selectedColor = color }
                        .border(
                            width = if (selectedColor == color) 3.dp else 0.dp,
                            color = if (selectedColor == color) Color.Black else Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {

                if (categoryName.isBlank() || selectedColor == null) {
                    errorMessage = "Please enter a category name and select a color."
                    return@Button
                }

                categoryViewModel.addCategory(
                    categoryName,
                    selectedTransactionType,
                    selectedColor!!
                ) { error ->
                    if (error != null) {
                        errorMessage = error
                    } else {
                        errorMessage = ""
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Category")
        }
    }
}
