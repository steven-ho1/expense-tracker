package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    // TEMPORARY. Just focused on UI for now
    val selectedFinancialTabIndex = rememberSaveable { mutableIntStateOf(0) }
    val selectedPeriodTabIndex = rememberSaveable { mutableIntStateOf(1) }
    val balance = 1000.00

    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = String.format(Locale.CANADA_FRENCH, "Solde : %.2f $", balance),
            modifier = Modifier.padding(5.dp)
        )

        TabRow(
            selectedTabIndex = selectedFinancialTabIndex.intValue,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            listOf("Dépenses", "Revenus").forEachIndexed { index, financialTab ->
                Tab(
                    selected = selectedFinancialTabIndex.intValue == index,
                    onClick = { selectedFinancialTabIndex.intValue = index }) {
                    Text(text = financialTab, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }

        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .padding(top = 10.dp)
        ) {
            TabRow(
                selectedTabIndex = selectedPeriodTabIndex.intValue
            ) {
                listOf(
                    "Jour",
                    "Semaine",
                    "Mois",
                    "Année",
                ).forEachIndexed { index, period ->
                    Tab(
                        selected = selectedPeriodTabIndex.intValue == index,
                        onClick = { selectedPeriodTabIndex.intValue = index }) {
                        Text(text = period, modifier = Modifier.padding(vertical = 10.dp))
                    }
                }

            }

            Text(
                text = "Pie Chart ici",
            )

            FloatingActionButton(
                onClick = { navController.navigate(ExpenseTrackerScreen.NewTransaction.name) },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.End),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

