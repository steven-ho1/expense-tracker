package com.inf8405.expensetracker.viewmodels

import android.graphics.Color as AndroidColor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.database.repositories.ChartsRepository
import com.inf8405.expensetracker.database.repositories.CategoryRepository
import com.inf8405.expensetracker.database.repositories.TransactionDetailsData
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.inf8405.expensetracker.models.DefaultCategories

// ViewModel pour gérer l'affichage du graphique et la sélection de périodes/type
class ChartsViewModel : ViewModel() {

    // Création des repositories
    private val categoryRepository = CategoryRepository(AppDatabase.instance.categoryDao())
    private val chartsRepository = ChartsRepository(AppDatabase.instance.transactionDao(), categoryRepository)

    // Stocke le type de transaction (input: aucun, output: TransactionType)
    private val _selectedType = MutableStateFlow(TransactionType.EXPENSES)
    val selectedType: StateFlow<TransactionType> = _selectedType

    // Stocke la période sélectionnée (input: aucun, output: String)
    private val _selectedPeriod = MutableStateFlow("Hebdomadaire")
    val selectedPeriod: StateFlow<String> = _selectedPeriod

    // Données du graphique (input: aucun, output: List<BarEntry>)
    private val _chartData = MutableStateFlow<List<BarEntry>>(emptyList())
    val chartData: StateFlow<List<BarEntry>> = _chartData.asStateFlow()

    // Étiquettes pour l'axe X (input: aucun, output: List<String>)
    private val _chartLabels = MutableStateFlow<List<String>>(emptyList())
    val chartLabels: StateFlow<List<String>> = _chartLabels.asStateFlow()

    // Détails des transactions pour une barre sélectionnée (input: aucun, output: TransactionDetailsData)
    private val _selectedDetails = MutableStateFlow<TransactionDetailsData?>(null)
    val selectedDetails: StateFlow<TransactionDetailsData?> = _selectedDetails.asStateFlow()

    // Couleurs des catégories pour le graphique (input: aucun, output: List<Int>)
    private val _categoryColors = MutableStateFlow<List<Int>>(emptyList())
    val categoryColors: StateFlow<List<Int>> = _categoryColors.asStateFlow()

    // Labels pour les segments du stacked bar chart (input: aucun, output: List<String>)
    private val _stackLabels = MutableStateFlow<List<String>>(emptyList())
    val stackLabels: StateFlow<List<String>> = _stackLabels.asStateFlow()



    init {
        loadTransactions()
        loadCategoryColors() 
    }

    // Input: TransactionType | Output: Mise à jour de _selectedType et recharge des données
    fun setTransactionType(type: TransactionType) {
        _selectedType.value = type
        loadTransactions()
    }

    // Input: période (String) | Output: Mise à jour de _selectedPeriod et recharge des données
    fun setPeriod(period: String) {
        _selectedPeriod.value = period
        loadTransactions()
    }

    // Charge les données agrégées du graphique (BarEntry, étiquettes, stackLabels)
    // Input: _selectedPeriod, _selectedType | Output: Mise à jour de _chartData, _chartLabels, _stackLabels
    private fun loadTransactions() {
        viewModelScope.launch {
            chartsRepository.getStackedTransactionsByPeriod(_selectedPeriod.value, _selectedType.value)
                .collect { barChartData ->
                    _chartData.value = barChartData.entries
                    _chartLabels.value = barChartData.labels
                    _stackLabels.value = barChartData.stackLabels
                }
        }
    }

    // Charge les couleurs des catégories en combinant les catégories par défaut et celles de la DB
    // Input: _selectedType | Output: Mise à jour de _categoryColors (List<Int>)
    private fun loadCategoryColors() {
        viewModelScope.launch {
            val dbCategories = categoryRepository.getCategories()
            val defaultCategories = when (_selectedType.value) {
                TransactionType.EXPENSES -> DefaultCategories.expenseCategories
                TransactionType.INCOME -> DefaultCategories.incomeCategories
            }
            val combinedCategories = (defaultCategories + dbCategories.filter { it.type == _selectedType.value })
                .distinctBy { it.name }
            _categoryColors.value = if (combinedCategories.isEmpty()) {
                listOf(AndroidColor.BLACK)
            } else {
                combinedCategories.map { AndroidColor.parseColor(it.color) }
            }
        }
    }

    // Charge les détails des transactions pour la barre sélectionnée
    // Input: index de la barre | Output: Mise à jour de _selectedDetails
    fun selectBarEntry(barIndex: Int) {
        viewModelScope.launch {
            chartsRepository.getTransactionDetailsByBar(_selectedPeriod.value, _selectedType.value, barIndex)
                .collect { details ->
                    _selectedDetails.value = details
                }
        }
    }
}
