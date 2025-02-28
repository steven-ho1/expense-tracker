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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.R
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.viewmodels.CategoryViewModel


@Composable
fun AddCategoryScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
) {
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel
    val colorNullableSaver: Saver<Color?, Long> = Saver(
        save = { color -> color?.value?.toLong() ?: -1L },
        restore = { value -> if (value == -1L) null else Color(value.toULong()) }
    )
    var categoryName by rememberSaveable { mutableStateOf("") }
    var selectedTransactionType by rememberSaveable { mutableStateOf(TransactionType.EXPENSES) }
    var selectedColor by rememberSaveable(stateSaver = colorNullableSaver) {
        mutableStateOf(
            null
        )
    }
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
        val maxChar = 50
        OutlinedTextField(
            value = categoryName,
            onValueChange = { input ->
                if (input.length <= maxChar) {
                    categoryName = input
                }
            },
            label = { Text("Nom de catégorie") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "${categoryName.length}/$maxChar",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Type de catégorie")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            RadioButton(
                selected = (selectedTransactionType == TransactionType.EXPENSES),
                onClick = { selectedTransactionType = TransactionType.EXPENSES }
            )
            Text(
                text = TransactionType.EXPENSES.label,
                modifier = Modifier
                    .clickable { selectedTransactionType = TransactionType.EXPENSES }
                    .padding(end = 16.dp)
            )
            RadioButton(
                selected = (selectedTransactionType == TransactionType.INCOME),
                onClick = { selectedTransactionType = TransactionType.INCOME }
            )
            Text(
                text = TransactionType.INCOME.label,
                modifier = Modifier
                    .clickable { selectedTransactionType = TransactionType.INCOME }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Choisir une couleur")
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
                    errorMessage = "Veuillez entrer un nom et choisir une couleur"
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
            Text("Ajouter la catégorie")
        }
    }
}
