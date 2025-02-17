package com.inf8405.expensetracker.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.database.repositories.CategoryRepository
import com.inf8405.expensetracker.models.TransactionType
import kotlinx.coroutines.launch

class AddCategoryViewModel(application: Application) : AndroidViewModel(application) {


    private fun colorToHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }

    private val repository: CategoryRepository by lazy {
        val dao = AppDatabase.instance.categoryDao()
        CategoryRepository(dao)
    }

    fun addCategory(
        categoryName: String,
        transactionType: com.inf8405.expensetracker.ui.screens.TransactionType,
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
            val categories = repository.getCategories()
            if (categories.any { it.name.equals(categoryName, ignoreCase = true) }) {
                onResult("This category name already exists")
            } else {
                repository.insertCategory(categoryEntity)
                onResult(null)
            }
        }
    }
}
