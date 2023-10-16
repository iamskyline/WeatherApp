package com.example.weatherapplication.components

import androidx.compose.ui.graphics.Color

class CustomColors {
    val mainColor = "#fcfcfc".toColor()
    val btnCityColor = "#85d249".toColor()
    val customGrayColor = "#f1f1f1".toColor()
    val cardBackGroundColor = "#e1e1e1".toColor()

    fun String.toColor(): Color {
        return Color(android.graphics.Color.parseColor(this))
    }
}