//package com.inf8405.expensetracker.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.github.mikephil.charting.data.BarEntry
//import com.inf8405.expensetracker.database.AppDatabase
//import com.inf8405.expensetracker.database.repositories.ChartsRepository
//import com.inf8405.expensetracker.database.repositories.TransactionDetailsData
//import com.inf8405.expensetracker.models.TransactionType
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class ChartsViewModel : ViewModel() {
//
//    // Initialisation du repository à partir du DAO obtenu via AppDatabase.instance.
//    private val chartsRepository: ChartsRepository = ChartsRepository(AppDatabase.instance.transactionDao())
//
//    private val _selectedType = MutableStateFlow(TransactionType.EXPENSES)
//    private val _selectedPeriod = MutableStateFlow("Hebdomadaire")
//    private val _chartData = MutableStateFlow<List<BarEntry>>(emptyList())
//    private val _selectedDetails = MutableStateFlow<TransactionDetailsData?>(null)
//
//    val selectedType: StateFlow<TransactionType> = _selectedType
//    val selectedPeriod: StateFlow<String> = _selectedPeriod
//    val chartData: StateFlow<List<BarEntry>> = _chartData.asStateFlow()
//    val selectedDetails: StateFlow<TransactionDetailsData?> = _selectedDetails.asStateFlow()
//
//    init {
//        loadTransactions()
//    }
//
//    fun setTransactionType(type: TransactionType) {
//        _selectedType.value = type
//        loadTransactions()
//    }
//
//    fun setPeriod(period: String) {
//        _selectedPeriod.value = period
//        loadTransactions()
//    }
//
//    private fun loadTransactions() {
//        viewModelScope.launch {
//            chartsRepository.getTransactionsByPeriod(_selectedPeriod.value, _selectedType.value)
//                .collect { _chartData.value = it }
//        }
//    }
//
//    fun selectBarEntry(barIndex: Int) {
//        viewModelScope.launch {
//            chartsRepository.getTransactionDetailsByBar(_selectedPeriod.value, _selectedType.value, barIndex)
//                .collect { _selectedDetails.value = it }
//        }
//    }
//}
