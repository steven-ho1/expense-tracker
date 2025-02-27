package com.inf8405.expensetracker.models

import com.inf8405.expensetracker.database.entities.CategoryEntity

object DefaultCategories {
    val expenseCategories = listOf(
        CategoryEntity(
            name = "Alimentation",
            type = TransactionType.EXPENSES,
            color = "#FF7300"
        ),
        CategoryEntity(
            name = "Transport",
            type = TransactionType.EXPENSES,
            color = "#FFC107"
        ),
        CategoryEntity(
            name = "Loisirs",
            type = TransactionType.EXPENSES,
            color = "#2196F3"
        ),
        CategoryEntity(
            name = "Logement",
            type = TransactionType.EXPENSES,
            color = "#9C27B0"
        )
    )

    val incomeCategories = listOf(
        CategoryEntity(
            name = "Salaire",
            type = TransactionType.INCOME,
            color = "#E91E63"
        ),
        CategoryEntity(
            name = "Prime",
            type = TransactionType.INCOME,
            color = "#52D726"
        ),
        CategoryEntity(
            name = "Investissements",
            type = TransactionType.INCOME,
            color = "#009688"
        ),
        CategoryEntity(
            name = "Cadeaux reçus",
            type = TransactionType.INCOME,
            color = "#FF7300"
        )
    )
}
