package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import androidx.compose.ui.viewinterop.AndroidView
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.viewmodels.ChartsViewModel
import android.graphics.Color

@Composable
fun ChartsScreen(
    chartsViewModel: ChartsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val transactions by chartsViewModel.transactions.collectAsState()
    val selectedType by chartsViewModel.selectedType.collectAsState()
    val selectedPeriod by chartsViewModel.selectedPeriod.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 📌 Onglets Dépenses / Revenus
        val tabs = listOf("Dépenses", "Revenus")

        TabRow(selectedTabIndex = if (selectedType == TransactionType.EXPENSES) 0 else 1) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = (index == 0 && selectedType == TransactionType.EXPENSES) ||
                               (index == 1 && selectedType == TransactionType.INCOME),
                    onClick = {
                        val type = if (index == 0) TransactionType.EXPENSES else TransactionType.INCOME
                        chartsViewModel.setTransactionType(type)
                    }
                ) {
                    Text(text = title, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }

        // 📌 Filtre par période
        val periods = listOf("Journalier", "Hebdomadaire", "Mensuel", "Annuel")

        TabRow(selectedTabIndex = periods.indexOf(selectedPeriod)) {
            periods.forEachIndexed { index, period ->
                Tab(
                    selected = selectedPeriod == period,
                    onClick = { chartsViewModel.setPeriod(period) }
                ) {
                    Text(text = period, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📌 Vérification si les transactions sont disponibles
        if (transactions.isEmpty()) {
            Text(
                text = "Aucune transaction disponible",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // 📌 Préparer les données pour le stacked bar chart
            val categoryGroupedTransactions = transactions
                .groupBy { it.category }
                .map { (category, transactions) ->
                    category to transactions.sumOf { it.amount }
                }

            val entries = categoryGroupedTransactions.mapIndexed { index, (category, totalAmount) ->
                BarEntry(index.toFloat(), floatArrayOf(totalAmount.toFloat())) // ✅ Stacked bars
            }

            // 📌 Création du jeu de données pour le stacked bar chart
            val dataSet = BarDataSet(entries, "Transactions").apply {
                setStackLabels(categoryGroupedTransactions.map { it.first }.toTypedArray()) // Labels des catégories
                colors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN)
            }

            // 📌 Affichage du Graphique BarChart
            AndroidView(
                factory = { context ->
                    BarChart(context).apply {
                        this.data = BarData(dataSet)
                        this.description.isEnabled = false
                        this.setFitBars(true)
                        this.invalidate()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 📌 Liste des détails sous le graphique
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // ✅ Correction pour éviter le crash "infinite constraints"
            ) {
                items(categoryGroupedTransactions.size) { index ->
                    val (category, totalAmount) = categoryGroupedTransactions[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "$category: ${totalAmount} $", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}
