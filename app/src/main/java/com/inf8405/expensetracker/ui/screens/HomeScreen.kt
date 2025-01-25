package com.inf8405.expensetracker.ui.screens

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Log.d("OKAY", "home")
    
    Text(text = "Page principale")
}