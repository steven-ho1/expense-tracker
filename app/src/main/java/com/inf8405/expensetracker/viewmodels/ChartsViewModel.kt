package com.inf8405.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.database.repositories.ChartsRepository
import com.inf8405.expensetracker.database.repositories.TransactionDetailsData
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChartsViewModel : ViewModel() {

   // Initialisation du repository à partir du DAO obtenu via AppDatabase.instance.
   private val chartsRepository: ChartsRepository = ChartsRepository(AppDatabase.instance.transactionDao())

   // État courant du type de transaction sélectionné (par défaut : Dépenses)
   private val _selectedType = MutableStateFlow(TransactionType.EXPENSES)
   val selectedType: StateFlow<TransactionType> = _selectedType

   // État courant de la période sélectionnée (par défaut : Hebdomadaire)
   private val _selectedPeriod = MutableStateFlow("Hebdomadaire")
   val selectedPeriod: StateFlow<String> = _selectedPeriod

   // Données du graphique (barres et labels)
   private val _chartData = MutableStateFlow<List<BarEntry>>(emptyList())
   val chartData: StateFlow<List<BarEntry>> = _chartData.asStateFlow()

   private val _chartLabels = MutableStateFlow<List<String>>(emptyList())
   val chartLabels: StateFlow<List<String>> = _chartLabels.asStateFlow()

   // Détails des transactions pour une barre sélectionnée
   private val _selectedDetails = MutableStateFlow<TransactionDetailsData?>(null)
   val selectedDetails: StateFlow<TransactionDetailsData?> = _selectedDetails.asStateFlow()

   init {
       loadTransactions()
   }

    /**
    * Met à jour le type de transaction sélectionné et recharge les transactions.
    * Input: TransactionType (Dépenses ou Revenus)
    * Output: Met à jour _selectedType et recharge _chartData et _chartLabels
    */
   fun setTransactionType(type: TransactionType) {
       _selectedType.value = type
       loadTransactions()
   }

    /**
    * Met à jour la période sélectionnée et recharge les transactions.
    * Input: String (période : "Journalier", "Hebdomadaire", "Mensuel", "Annuel")
    * Output: Met à jour _selectedPeriod et recharge _chartData et _chartLabels
    */
   fun setPeriod(period: String) {
       _selectedPeriod.value = period
       loadTransactions()
   }

    /**
    * Charge les transactions et met à jour les données du graphique.
    * Input: Aucun (utilise les valeurs actuelles de _selectedPeriod et _selectedType)
    * Output: Met à jour _chartData avec les entrées du graphique et _chartLabels
    */
   private fun loadTransactions() {
    viewModelScope.launch {
        chartsRepository.getTransactionsByPeriod(_selectedPeriod.value, _selectedType.value)
            .collect { barChartData ->
                _chartData.value = barChartData.entries
                _chartLabels.value = barChartData.labels
            }
        }
    }

   /**
    * Charge les détails des transactions pour une barre sélectionnée dans le graphique.
    * Input: Int (index de la barre sélectionnée)
    * Output: Met à jour _selectedDetails avec les transactions associées à cette période
    */
   fun selectBarEntry(barIndex: Int) {
       viewModelScope.launch {
           chartsRepository.getTransactionDetailsByBar(_selectedPeriod.value, _selectedType.value, barIndex)
               .collect { _selectedDetails.value = it }
       }
   }
}
