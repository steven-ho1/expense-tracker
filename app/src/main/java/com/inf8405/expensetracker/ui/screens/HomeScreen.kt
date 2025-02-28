package com.inf8405.expensetracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.ui.components.TransactionGroupList
import com.inf8405.expensetracker.ui.components.TransactionPieChart
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.utils.groupByCategory
import com.inf8405.expensetracker.viewmodels.CategoryViewModel
import com.inf8405.expensetracker.viewmodels.TransactionViewModel
import filterTransactionsByPeriod
import getPeriodText
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
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

    val transactions = if (selectedTransactionTabIndex == 0) expenses else income
    val filteredTransactions = filterTransactionsByPeriod(transactions, selectedPeriodTabIndex)
    val categories = if (selectedTransactionTabIndex == 0) expenseCategories else incomeCategories

    val transactionGroups = groupByCategory(filteredTransactions, categories)

    Column(
        modifier = modifier
            .padding(10.dp)
    ) {
        Text(
            text = String.format(Locale.CANADA_FRENCH, "Solde : %.2f$", balance),
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
                    "Journalier",
                    "Hebdomadaire",
                    "Mensuel",
                    "Annuel",
                ).forEachIndexed { index, period ->
                    Tab(
                        selected = selectedPeriodTabIndex == index,
                        onClick = { selectedPeriodTabIndex = index }) {
                        Text(text = period, modifier = modifier.padding(vertical = 10.dp))
                    }
                }

            }

            Text(
                text = getPeriodText(selectedPeriodTabIndex),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                TransactionPieChart(
                    transactionGroups,
                    modifier = Modifier
                        .align(Alignment.Center)
                )

                FloatingActionButton(
                    onClick = {
                        navController.navigate(ExpenseTrackerScreen.NewTransaction.name)
                    },
                    modifier = modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter une transaction")
                }
            }
        }

        TransactionGroupList(transactionGroups)
    }
}




