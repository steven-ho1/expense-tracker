package com.inf8405.expensetracker.database.repositories

import com.inf8405.expensetracker.database.dao.CategoryDao
import com.inf8405.expensetracker.database.entities.CategoryEntity

class CategoryRepository(private val categoryDao: CategoryDao) {
    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    suspend fun getCategories(): List<CategoryEntity> {
        return categoryDao.getCategories()
    }
}