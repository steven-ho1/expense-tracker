package com.inf8405.expensetracker

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.inf8405.expensetracker.database.AppDatabase
import com.inf8405.expensetracker.ui.ExpenseTrackingApp
import com.inf8405.expensetracker.ui.screens.SplashScreen
import com.inf8405.expensetracker.ui.theme.ExpenseTrackerTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            AppDatabase.init(context = this)

            val config = resources.configuration
            config.setLocale(Locale.CANADA_FRENCH)

            enableEdgeToEdge()

            setContent {
                ExpenseTrackerTheme {
                    var showSplash by rememberSaveable { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen(onTimeout = { showSplash = false })
                    } else {
                        ExpenseTrackingApp()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("UnexpectedError", "Unexpected error", e)
        }
    }
}
