package com.inf8405.expensetracker.viewmodels

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

    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.insertCategory(category)
            loadCategories()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val allCategories = categoryRepository.getCategories()

            _expenseCategories.value = allCategories.filter { it.type == TransactionType.EXPENSES }
            _incomeCategories.value = allCategories.filter { it.type == TransactionType.INCOME }
        }
    }
}

