package com.example.weatherapplication.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapplication.R
import com.example.weatherapplication.comfortaaMediumText
import com.example.weatherapplication.methods
import com.example.weatherapplication.models.WeatherModel

class ForecastComponents {
    var customColors = CustomColors()
    var fonts = Fonts()

    @Composable
    fun weatherForecastArea(city: MutableState<String>, context: Context) {
        var daysList = remember {
            mutableStateOf(listOf<WeatherModel>())
        }

        var currentDay = remember {
            mutableStateOf(
                WeatherModel(
                    "", "", "", "", "", "", "",
                    "", "", "", ""
                )
            )
        }

        if (city.value == "Выбрать город") defaultWeatherForecastCard()
        else {
            methods.getResult(city, context, daysList, currentDay)
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(1) {
                    Box(
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .fillParentMaxWidth()
                    ) {
                        currentWeatherForecastCard(city, context, currentDay)
                    }
                    Box(
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .fillParentMaxWidth()
                    ) {
                        daysWeatherForecast(city, context, daysList)
                    }
                }
            }
        }
    }

    @Composable
    fun currentWeatherForecastCard(
        city: MutableState<String>, context: Context,
        currentDay: MutableState<WeatherModel>
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(customColors.cardBackGroundColor)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(containerColor = customColors.customGrayColor)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = "https:${currentDay.value.icon}",
                        contentDescription = "Weather_icon",
                        modifier = Modifier.size(90.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${currentDay.value.currentTemp}°C", style = TextStyle(
                            fontFamily = fonts.comfortaaBold,
                            fontSize = 26.sp
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "Страна:")
                    comfortaaMediumText(text = "${currentDay.value.country}")
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "Регион:")
                    comfortaaMediumText(text = "${currentDay.value.region}")
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "Погода:")
                    comfortaaMediumText(text = "${currentDay.value.condition}")
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "Направление ветра:")
                    comfortaaMediumText(text = "${currentDay.value.wind_dir}")
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "Ощущается как:")
                    comfortaaMediumText(text = "${currentDay.value.feelsLike}°C")
                }
            }
        }
    }

    @Composable
    fun daysWeatherForecast(
        city: MutableState<String>, context: Context,
        daysList: MutableState<List<WeatherModel>>
    ) {
        Box(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .clip(shape = RoundedCornerShape(13.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(customColors.cardBackGroundColor)
                    .padding(10.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                ) {
                    itemsIndexed(
                        daysList.value
                    ) { _, item ->
                        dayCard(item)
                    }
                }
            }
        }
    }

    @Composable
    fun dayCard(weatherObject: WeatherModel) {
        Card(
            modifier = Modifier
                .padding(top = 5.dp)
                .background(customColors.customGrayColor),
            colors = CardDefaults.cardColors(containerColor = customColors.customGrayColor)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https:${weatherObject.icon}", contentDescription = "Weather_Icon",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "${weatherObject.date}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    comfortaaMediumText(text = "${weatherObject.condition}")
                    comfortaaMediumText(
                        text = "${weatherObject.mintemp_c}°C " +
                                "/ ${weatherObject.maxtemp_c}°C"
                    )
                }
            }
        }
    }

    @Composable
    fun defaultWeatherForecastCard() {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = customColors.customGrayColor)
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
                    style = TextStyle(fontFamily = fonts.comfortaaMedium, fontSize = 20.sp)
                )
                Text(
                    text = "Для начала, выберите город", style = TextStyle(
                        fontFamily = fonts.comfortaaMedium, fontSize = 20.sp
                    )
                )
            }
        }
    }
}



