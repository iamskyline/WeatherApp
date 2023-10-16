package com.example.weatherapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapplication.R
import com.example.weatherapplication.comfortaaMedium
import com.example.weatherapplication.customGrayColor

@Composable
fun defaultWeatherForecastCard() {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = customGrayColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val image = painterResource(id = R.drawable.earth)
            Image(
                painter = image, contentDescription = "Нет результатов",
                modifier = Modifier.padding(end = 35.dp, bottom = 20.dp)
            )
            Text(
                text = "Нечего показывать!", textAlign = TextAlign.Center,
                style = TextStyle(fontFamily = comfortaaMedium, fontSize = 20.sp)
            )
            Text(
                text = "Для начала, выберите город", style = TextStyle(
                    fontFamily = comfortaaMedium, fontSize = 20.sp
                )
            )
        }
    }
}
