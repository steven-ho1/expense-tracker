package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.utils.toColor
import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import com.inf8405.expensetracker.viewmodels.TransactionViewModel
import java.util.Locale

@Composable
fun HomeScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val transactionViewModel: TransactionViewModel = mainViewModelsWrapper.transactionViewModel
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel
    val balance by transactionViewModel.balance.collectAsState()
    val expenses by transactionViewModel.expenses.collectAsState()
    val income by transactionViewModel.income.collectAsState()
    val expenseCategories by categoryViewModel.expenseCategories.collectAsState()
    val incomeCategories by categoryViewModel.incomeCategories.collectAsState()

    var selectedTransactionTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedPeriodTabIndex by rememberSaveable { mutableIntStateOf(1) }

    // TODO Need period
    val expensesByCategory = groupByCategory(expenses, expenseCategories)
    val incomeByCategory = groupByCategory(income, incomeCategories)

    Column(
        modifier = modifier
            .padding(10.dp)
    ) {
        Text(
            text = String.format(Locale.CANADA_FRENCH, "Solde : %.2f $", balance),
            modifier = modifier.padding(5.dp)
        )

        TabRow(
            selectedTabIndex = selectedTransactionTabIndex,
            modifier = modifier
                .padding(horizontal = 20.dp)
        ) {
            listOf(
                TransactionType.EXPENSES.label,
                TransactionType.INCOME.label
            ).forEachIndexed { index, financialTab ->
                Tab(
                    selected = selectedTransactionTabIndex == index,
                    onClick = { selectedTransactionTabIndex = index }) {
                    Text(text = financialTab, modifier = modifier.padding(vertical = 10.dp))
                }
            }
        }

        Spacer(modifier = modifier.height(10.dp))

        ElevatedCard {
            TabRow(
                selectedTabIndex = selectedPeriodTabIndex
            ) {
                listOf(
                    "Jour",
                    "Semaine",
                    "Mois",
                    "Année",
                ).forEachIndexed { index, period ->
                    Tab(
                        selected = selectedPeriodTabIndex == index,
                        onClick = { selectedPeriodTabIndex = index }) {
                        Text(text = period, modifier = modifier.padding(vertical = 10.dp))
                    }
                }

            }

            Text(
                text = "Pie Chart ici",
            )

            FloatingActionButton(
                onClick = {
                    navController.navigate(ExpenseTrackerScreen.NewTransaction.name)
                },
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.End),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

        LazyColumn(
            modifier = modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            val transactions =
                if (selectedTransactionTabIndex == 0) expensesByCategory else incomeByCategory
            transactions.forEach { transaction ->
                item {
                    ElevatedCard(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(transaction.third.toColor())
                            )

                            Text(
                                text = String.format(
                                    Locale.CANADA_FRENCH,
                                    "%s : %.2f $",
                                    transaction.first,
                                    transaction.second
                                )
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(10.dp))
                }
            }
        }
    }
}

fun groupByCategory(
    transactions: List<TransactionEntity>,
    categories: List<CategoryEntity>
): List<Triple<String, Double, String>> {
    return transactions
        .groupBy { it.category }
        .map { (categoryName, transactions) ->
            val totalAmount = transactions.sumOf { it.amount }
            val categoryColor = categories.find { it.name == categoryName }?.color ?: "#000000"

            Triple(categoryName, totalAmount, categoryColor)
        }
}