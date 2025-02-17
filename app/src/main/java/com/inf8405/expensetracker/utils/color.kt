package com.inf8405.expensetracker.utils


import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

// Pour convertir une chaîne hexadécimale en Color
fun String.toColor(): Color {
    return Color(AndroidColor.parseColor(this))
}