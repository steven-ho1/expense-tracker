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
    private val categoryRepository: CategoryRepository =
        CategoryRepository(AppDatabase.instance.categoryDao())
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
        val defaultExpenseCategories = listOf(
            CategoryEntity(
                name = "Alimentation",
                type = TransactionType.EXPENSES,
                color = "#FF5733" // Orange
            ),
            CategoryEntity(
                name = "Transport",
                type = TransactionType.EXPENSES,
                color = "#33FF57" // Vert
            ),
            CategoryEntity(
                name = "Loisirs",
                type = TransactionType.EXPENSES,
                color = "#FF33A1" // Rose
            ),
            CategoryEntity(
                name = "Logement",
                type = TransactionType.EXPENSES,
                color = "#3357FF" // Bleu
            )
        )

        val defaultIncomeCategories = listOf(
            CategoryEntity(
                name = "Salaire",
                type = TransactionType.INCOME,
                color = "#4CAF50" // Vert
            ),
            CategoryEntity(
                name = "Prime",
                type = TransactionType.INCOME,
                color = "#FFEB3B" // Jaune
            ),
            CategoryEntity(
                name = "Investissements",
                type = TransactionType.INCOME,
                color = "#2196F3" // Bleu
            ),
            CategoryEntity(
                name = "Cadeaux reçus",
                type = TransactionType.INCOME,
                color = "#E91E63" // Rose
            )
        )

        viewModelScope.launch {
            val databaseCategories = categoryRepository.getCategories()
            val allExpenseCategories =
                defaultExpenseCategories + databaseCategories.filter { it.type == TransactionType.EXPENSES }
            val allIncomeCategories =
                defaultIncomeCategories + databaseCategories.filter { it.type == TransactionType.INCOME }

            _expenseCategories.value = allExpenseCategories
            _incomeCategories.value = allIncomeCategories
        }
    }

    private fun colorToHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }
}

