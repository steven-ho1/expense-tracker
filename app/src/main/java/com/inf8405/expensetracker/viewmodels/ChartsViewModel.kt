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


class ChartsViewModel : ViewModel() {
    private val categoryRepository = CategoryRepository(AppDatabase.instance.categoryDao())
    private val chartsRepository = ChartsRepository(AppDatabase.instance.transactionDao(), categoryRepository)

    private val _selectedType = MutableStateFlow(TransactionType.EXPENSES)
    val selectedType: StateFlow<TransactionType> = _selectedType

    private val _selectedPeriod = MutableStateFlow("Hebdomadaire")
    val selectedPeriod: StateFlow<String> = _selectedPeriod

    private val _chartData = MutableStateFlow<List<BarEntry>>(emptyList())
    val chartData: StateFlow<List<BarEntry>> = _chartData.asStateFlow()

    private val _chartLabels = MutableStateFlow<List<String>>(emptyList())
    val chartLabels: StateFlow<List<String>> = _chartLabels.asStateFlow()

    // Détails des transactions pour une barre sélectionnée
    private val _selectedDetails = MutableStateFlow<TransactionDetailsData?>(null)
    val selectedDetails: StateFlow<TransactionDetailsData?> = _selectedDetails.asStateFlow()

    // Couleurs des catégories
    private val _categoryColors = MutableStateFlow<List<Int>>(emptyList())
    val categoryColors: StateFlow<List<Int>> = _categoryColors.asStateFlow()

    private val _stackLabels = MutableStateFlow<List<String>>(emptyList())
    val stackLabels: StateFlow<List<String>> = _stackLabels.asStateFlow()

    init {
        loadTransactions()
        loadCategoryColors()
    }

    fun setTransactionType(type: TransactionType) {
        _selectedType.value = type
        loadTransactions()
    }

    fun setPeriod(period: String) {
        _selectedPeriod.value = period
        loadTransactions()
    }

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
    
    

    fun selectBarEntry(barIndex: Int) {
        viewModelScope.launch {
            chartsRepository.getTransactionDetailsByBar(_selectedPeriod.value, _selectedType.value, barIndex)
                .collect { details ->
                    _selectedDetails.value = details
                }
        }
    }
}
