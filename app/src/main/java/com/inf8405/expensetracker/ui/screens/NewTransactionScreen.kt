package com.inf8405.expensetracker.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.R
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.viewmodels.TransactionViewModel
import java.util.Date

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
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = modifier.padding(vertical = 20.dp)
        )

        Button(
            onClick = {
                val transactionType = if (selectedTransactionTabIndex == 0)
                    TransactionType.EXPENSES
                else
                    TransactionType.INCOME

                // TODO Ajouter catégories et revoir date
                val newTransaction = TransactionEntity(
                    amount = amountInput.toDoubleOrNull() ?: 0.0,
                    type = transactionType,
                    category = "TODO",
                    date = Date().toString()
                )
                transactionViewModel.addTransaction(newTransaction)
                navController.navigateUp()
            },
            enabled = isAmountValid
        ) {
            Text(text = "Ajouter")
        }
    }
}

@Composable
fun NumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        singleLine = true,
        label = { Text(stringResource(label)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                "Icon",
            )
        },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}