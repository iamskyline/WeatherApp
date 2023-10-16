package com.example.weatherapplication.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapplication.R
import com.example.weatherapplication.citiesList
import com.example.weatherapplication.components.ForecastComponents

class GreetingComponents {
    var customColors = CustomColors()
    var forecastComponents = ForecastComponents()
    var fonts = Fonts()

    @Composable
    fun greetingUser() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val preview = painterResource(id = R.drawable.preview)
            Image(
                painter = preview,
                contentDescription = "Логотип",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 30.dp, bottom = 15.dp)
            )
            Box(modifier = Modifier.padding(bottom = 30.dp)) {
                Text(
                    text = "Добро пожаловать!", style = TextStyle(
                        fontFamily = fonts.comfortaaBold,
                        fontSize = 30.sp
                    )
                )
            }
        }
    }

    @Composable
    fun citySelector(context: Context) {
        var menuState = remember {
            mutableStateOf(false)
        }

        var cityState = remember {
            mutableStateOf("Выбрать город")
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = customColors.btnCityColor,
                        contentColor = Color.White
                    ),
                    onClick = { menuState.value = !menuState.value }
                ) {
                    Text(
                        text = cityState.value, style = TextStyle(
                            fontFamily = fonts.comfortaaBold,
                            fontSize = 18.sp
                        )
                    )
                }
            }
            Box() {
                DropdownMenu(
                    expanded = menuState.value,
                    onDismissRequest = { menuState.value = !menuState.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(customColors.customGrayColor),
                ) {
                    citiesList.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item, style = TextStyle(
                                        fontFamily = fonts.comfortaaMedium,
                                        fontSize = 18.sp
                                    )
                                )
                            },
                            onClick = {
                                cityState.value = item
                                menuState.value = !menuState.value
                                //getResult(cityState)
                            },
                        )
                    }
                }
            }
            forecastComponents.weatherForecastArea(cityState, context)
        }
    }
}