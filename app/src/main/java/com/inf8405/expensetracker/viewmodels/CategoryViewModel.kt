package com.inf8405.expensetracker.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.database.repositories.CategoryRepository
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val categoryRepository: CategoryRepository = CategoryRepository(AppDatabase.instance.categoryDao())
    private val _expenseCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    private val _incomeCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())

    // Explication dans TransactionViewModel
    val expenseCategories: StateFlow<List<CategoryEntity>> = _expenseCategories
    val incomeCategories: StateFlow<List<CategoryEntity>> = _incomeCategories

    init {
        loadCategories()
    }

    fun addCategory(
        categoryName: String,
        transactionType: TransactionType,
        color: Color,
        onResult: (errorMessage: String?) -> Unit
    ) {

        val dbTransactionType = TransactionType.valueOf(transactionType.name)
        val colorHex = colorToHex(color)

        val categoryEntity = CategoryEntity(
            name = categoryName,
            type = dbTransactionType,
            color = colorHex
        )

        viewModelScope.launch {
            val categories = categoryRepository.getCategories()
            if (categories.any { it.name.equals(categoryName, ignoreCase = true) }) {
                onResult("This category name already exists")
            } else {
                categoryRepository.insertCategory(categoryEntity)
                onResult(null)
                loadCategories()
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val allCategories = categoryRepository.getCategories()

            _expenseCategories.value = allCategories.filter { it.type == TransactionType.EXPENSES }
            _incomeCategories.value = allCategories.filter { it.type == TransactionType.INCOME }
        }
    }

    private fun colorToHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }
}

