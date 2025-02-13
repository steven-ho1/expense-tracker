package com.inf8405.expensetracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.inf8405.expensetracker.ui.components.DatePickerModal
import com.inf8405.expensetracker.ui.components.NumberField
import com.inf8405.expensetracker.viewmodels.TransactionViewModel
import formatUtcMillisToString

@SuppressLint("SimpleDateFormat")
@Composable
fun NewTransactionScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel;
    val transactionViewModel: TransactionViewModel = mainViewModelsWrapper.transactionViewModel;

    var amountInput by rememberSaveable { mutableStateOf("") }
    var selectedTransactionTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val isAmountValid = amountInput.toDoubleOrNull() != null

    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
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
                    onClick = { selectedTransactionTabIndex = index }) {
                    Text(text = financialTab, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }

        NumberField(
            leadingIcon = R.drawable.attach_money_24dp,
            value = amountInput,
            onValueChange = { amountInput = it },
            label = R.string.amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            modifier = modifier.padding(top = 20.dp)
        )

        if (showDatePicker) {
            DatePickerModal(
                selectedDate = selectedDateMillis,
                onDateSelected = { dateMillis ->
                    selectedDateMillis = dateMillis
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(16.dp)
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
        }

        Button(
            onClick = {
                val transactionType = if (selectedTransactionTabIndex == 0)
                    TransactionType.EXPENSES
                else
                    TransactionType.INCOME

                // TODO Ajouter catégories
                val newTransaction = TransactionEntity(
                    amount = amountInput.toDoubleOrNull() ?: 0.0,
                    type = transactionType,
                    category = "TODO",
                    date = selectedDateMillis!!
                )
                transactionViewModel.addTransaction(newTransaction)
                navController.navigateUp()
            },
            enabled = isAmountValid && selectedDateMillis !== null
        ) {
            Text(text = "Ajouter la transaction")
        }
    }
}

