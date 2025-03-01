package com.inf8405.expensetracker.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.database.repositories.CategoryRepository
import com.inf8405.expensetracker.models.DefaultCategories
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val categoryRepository: CategoryRepository =
        CategoryRepository(AppDatabase.instance.categoryDao())
    private val _expenseCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    private val _incomeCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())

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

        val cleanedCategoryName = categoryName.trim().replace(Regex("\\s+"), " ")
        val categoryEntity = CategoryEntity(
            name = cleanedCategoryName,
            type = dbTransactionType,
            color = colorHex
        )

        viewModelScope.launch {
            val categories = expenseCategories.value + incomeCategories.value
            if (categories.any { it.name.equals(cleanedCategoryName, ignoreCase = true) }) {
                onResult("Le nom de catégorie est déjà pris")
            } else {
                categoryRepository.insertCategory(categoryEntity)
                onResult(null)
                loadCategories()
            }
        }
    }

    private fun loadCategories() {


        viewModelScope.launch {
            val databaseCategories = categoryRepository.getCategories()
            val allExpenseCategories =
                DefaultCategories.expenseCategories + databaseCategories.filter { it.type == TransactionType.EXPENSES }
            val allIncomeCategories =
                DefaultCategories.incomeCategories + databaseCategories.filter { it.type == TransactionType.INCOME }

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