package com.inf8405.expensetracker.ui.screens

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.inf8405.expensetracker.models.TransactionType
import com.inf8405.expensetracker.viewmodels.ChartsViewModel
import com.inf8405.expensetracker.database.entities.TransactionEntity
import com.inf8405.expensetracker.database.repositories.TransactionDetailsData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import formatUtcMillisToString

// Fonction utilitaire pour formater la date d'une transaction.
fun formatTransactionDate(dateString: String): String {
   val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
   val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
   val date = LocalDate.parse(dateString, inputFormatter)
   return date.format(outputFormatter)
}

@Composable
fun ChartsScreen(
   chartsViewModel: ChartsViewModel = viewModel(),
   modifier: Modifier = Modifier
) {
   val chartData by chartsViewModel.chartData.collectAsState(initial = emptyList<BarEntry>())
   val chartLabels by chartsViewModel.chartLabels.collectAsState(initial = emptyList<String>())
   val selectedType by chartsViewModel.selectedType.collectAsState(initial = TransactionType.EXPENSES)
   val selectedPeriod by chartsViewModel.selectedPeriod.collectAsState(initial = "Hebdomadaire")
   val selectedDetailsData by chartsViewModel.selectedDetails.collectAsState(initial = null as TransactionDetailsData?)

   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(16.dp)
   ) {
       // Onglets pour sélectionner Dépenses / Revenus
       val tabs = listOf("Dépenses", "Revenus")
       TabRow(selectedTabIndex = if (selectedType == TransactionType.EXPENSES) 0 else 1) {
           tabs.forEachIndexed { index, title ->
               Tab(
                   selected = (index == 0 && selectedType == TransactionType.EXPENSES) ||
                              (index == 1 && selectedType == TransactionType.INCOME),
                   onClick = {
                       val type = if (index == 0) TransactionType.EXPENSES else TransactionType.INCOME
                       chartsViewModel.setTransactionType(type)  // Utilisez chartsViewModel, et non chartsViewModels
                   }
               ) {
                   Text(text = title)
               }
           }
       }

       // Onglets pour sélectionner la période
       val periods = listOf("Journalier", "Hebdomadaire", "Mensuel", "Annuel")
       TabRow(selectedTabIndex = periods.indexOf(selectedPeriod)) {
           periods.forEach { period ->
               Tab(
                   selected = selectedPeriod == period,
                   onClick = { chartsViewModel.setPeriod(period) }
               ) {
                   Text(text = period)
               }
           }
       }

       Spacer(modifier = Modifier.height(16.dp))

       // Affichage du graphique via AndroidView
       AndroidView(
           factory = { context ->
               BarChart(context).apply {
                   val dataSet = BarDataSet(chartData, "Transactions").apply {
                       colors = listOf(
                           Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA
                       )
                       setDrawValues(false) // Désactive l'affichage des valeurs au-dessus des barres
                   }
                   this.data = BarData(dataSet)
                   this.description.isEnabled = false

                   // Configuration de l'axe X pour afficher les dates sous les barres
                   this.xAxis.apply {
                       valueFormatter = IndexAxisValueFormatter(chartLabels)
                       granularity = 1f
                       isGranularityEnabled = true
                       position = XAxis.XAxisPosition.BOTTOM
                       setDrawGridLines(false)
                   }

                   this.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                       override fun onValueSelected(e: Entry?, h: Highlight?) {
                           e?.let {
                               chartsViewModel.selectBarEntry(it.x.toInt())
                           }
                       }
                       override fun onNothingSelected() {
                           chartsViewModel.selectBarEntry(-1)
                       }
                   })
                   this.invalidate()
               }
           },
           update = { chart ->
               val dataSet = BarDataSet(chartData, "Transactions").apply {
                   colors = listOf(
                       Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA
                   )
                   setDrawValues(false)
               }
               chart.data = BarData(dataSet)
               chart.xAxis.apply {
                   valueFormatter = IndexAxisValueFormatter(chartLabels)
                   granularity = 1f
                   isGranularityEnabled = true
               }
               chart.invalidate()
           },
           modifier = Modifier
               .fillMaxWidth()
               .height(300.dp)
       )

       // Affichage des détails des transactions (période et liste)
        // Affichage amélioré des détails des transactions (période et liste)
        selectedDetailsData?.let { detailsData ->
            if (detailsData.transactions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Période : ${detailsData.periodRange}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Pour les listes longues, vous pouvez utiliser LazyColumn
                        detailsData.transactions.forEach { transaction ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = transaction.category,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${transaction.amount}$",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatTransactionDate(formatUtcMillisToString(transaction.date)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}