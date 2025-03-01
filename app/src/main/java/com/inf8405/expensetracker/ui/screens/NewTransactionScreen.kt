package com.inf8405.expensetracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.R
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.ui.components.CategoryList
import com.inf8405.expensetracker.ui.components.DatePickerModal
import com.inf8405.expensetracker.ui.components.NumberField
import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import com.inf8405.expensetracker.viewmodels.TransactionViewModel
import formatUtcMillisToString

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewTransactionScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel
    val transactionViewModel: TransactionViewModel = mainViewModelsWrapper.transactionViewModel

    var amountInput by rememberSaveable { mutableStateOf("") }
    var selectedTransactionTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val isAmountValid = amountInput.toDoubleOrNull() != null

    val expenseCategories by categoryViewModel.expenseCategories.collectAsState()
    val incomeCategories by categoryViewModel.incomeCategories.collectAsState()
    val categories = if (selectedTransactionTabIndex == 0) expenseCategories else incomeCategories
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Onglets pour choisir le type de la transaction (dépenses ou revenus)
        TabRow(
            selectedTabIndex = selectedTransactionTabIndex,
            modifier = Modifier
                .padding(horizontal = 5.dp)
        ) {
            listOf(
                TransactionType.EXPENSES.label,
                TransactionType.INCOME.label
            ).forEachIndexed { index, financialTab ->
                Tab(
                    selected = selectedTransactionTabIndex == index,
                    onClick = {
                        selectedTransactionTabIndex = index
                        selectedCategory = null
                    }) {
                    Text(text = financialTab, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }

        Spacer(modifier = modifier.height(20.dp))

        // Champs pour indique la valeur de la transaction
        NumberField(
            leadingIcon = R.drawable.attach_money_24dp,
            value = amountInput,
            onValueChange = { amountInput = it },
            label = R.string.amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = modifier.height(20.dp))

        // Liste des catégories disponibles, incluant un bouton pour ajouter une nouvelle catégorie
        CategoryList(
            navController = navController,
            categories = categories,
            selectedCategory = selectedCategory
        ) { category ->
            selectedCategory = category
        }

        Spacer(modifier = modifier.height(20.dp))

        // Affichage et sélection de la date à partir d'un date picker
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (selectedDateMillis != null) {
                Text(formatUtcMillisToString(selectedDateMillis!!))
            } else {
                Text("Aucune date choisie")
            }

            IconButton(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .background(Color.Gray, shape = CircleShape),
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Calendar", tint = Color.White)
            }

            if (showDatePicker) {
                DatePickerModal(
                    selectedDate = selectedDateMillis,
                    onDateSelected = { dateMillis ->
                        selectedDateMillis = dateMillis
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }

        Spacer(modifier = modifier.height(30.dp))

        // Bouton de soumission de la nouvelle transaction
        Button(
            onClick = {
                val transactionType = if (selectedTransactionTabIndex == 0)
                    TransactionType.EXPENSES
                else
                    TransactionType.INCOME

                val newTransaction = TransactionEntity(
                    amount = amountInput.toDoubleOrNull() ?: 0.0,
                    type = transactionType,
                    category = selectedCategory!!,
                    date = selectedDateMillis!!
                )
                transactionViewModel.addTransaction(newTransaction)
                navController.navigateUp()
            },
            enabled = isAmountValid && selectedDateMillis !== null && selectedCategory !== null
        ) {
            Text(text = "Ajouter la transaction")
        }
    }
}

