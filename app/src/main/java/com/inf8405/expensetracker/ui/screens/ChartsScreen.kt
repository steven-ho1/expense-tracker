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

// Fonction utilitaire pour formater la date d'une transaction au format "dd-MM-yyyy".
fun formatTransactionDate(dateString: String): String {
   val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
   val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
   val date = LocalDate.parse(dateString, inputFormatter)
   return date.format(outputFormatter)
}

@Composable
fun ChartsScreen(
   chartsViewModel: ChartsViewModel = viewModel()
) {
   // Collecte des données depuis le ViewModel
   val chartData by chartsViewModel.chartData.collectAsState(initial = emptyList<BarEntry>())
   val chartLabels by chartsViewModel.chartLabels.collectAsState(initial = emptyList<String>())
   val categoryColors by chartsViewModel.categoryColors.collectAsState()
   val stackLabels by chartsViewModel.stackLabels.collectAsState(initial = emptyList<String>())

   // Etat pour le type et la période sélectionnés
   val selectedType by chartsViewModel.selectedType.collectAsState(initial = TransactionType.EXPENSES)
   val selectedPeriod by chartsViewModel.selectedPeriod.collectAsState(initial = "Hebdomadaire")
   val selectedDetailsData by chartsViewModel.selectedDetails.collectAsState(initial = null as TransactionDetailsData?)

   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(16.dp)
   ) {
       // Onglets pour choisir entre Dépenses et Revenus
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
                   Text(text = title)
               }
           }
       }

       // Onglets pour choisir la période d'affichage
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

       // Affichage du graphique en barres à l'aide de AndroidView et MPAndroidChart
       AndroidView(
           factory = { context ->
               BarChart(context).apply {
                   // Création du DataSet avec les données du graphique
                   val dataSet = BarDataSet(chartData, "Transactions").apply {
                       // Applique les couleurs dynamiques obtenues depuis le ViewModel
                       colors = categoryColors
                       setDrawValues(false)
                       setStackLabels(stackLabels.toTypedArray())
                   }
                   // Affectation des données et configuration du graphique
                   this.data = BarData(dataSet)
                   this.description.isEnabled = false
                   this.xAxis.apply {
                       // Affiche les dates sous forme d'étiquettes sur l'axe des X
                       valueFormatter = IndexAxisValueFormatter(chartLabels)
                       granularity = 1f
                       isGranularityEnabled = true
                       position = XAxis.XAxisPosition.BOTTOM
                       setDrawGridLines(false)
                   }
                   // Gère la sélection d'une barre pour afficher les détails
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
               // Mise à jour du DataSet lors d'un recomposition
               val dataSet = BarDataSet(chartData, "Transactions").apply {
                   colors = categoryColors
                   setDrawValues(false)
                   setStackLabels(stackLabels.toTypedArray())
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

       // Affichage des détails des transactions pour la barre sélectionnée
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
                       // Affiche chaque transaction sous forme de ligne dans la carte
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
