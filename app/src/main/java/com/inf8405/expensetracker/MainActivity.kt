package com.inf8405.expensetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.ui.ExpenseTrackingApp
import com.inf8405.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            AppDatabase.init(context = this)

            enableEdgeToEdge()
            setContent {
                ExpenseTrackerTheme {
                    ExpenseTrackingApp()
                }
            }
        } catch (e: Exception) {
            Log.e("UnexpectedError", "Unexpected error", e)
        }

    }
}