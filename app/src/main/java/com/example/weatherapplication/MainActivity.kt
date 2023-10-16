package com.example.weatherapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapplication.components.defaultWeatherForecastCard
import com.example.weatherapplication.models.WeatherModel
import org.json.JSONObject

val comfortaaBold = FontFamily(
    Font(R.font.comfortaa_bold)
)
val comfortaaRegular = FontFamily(
    Font(R.font.comfortaa_regular)
)
val comfortaaMedium = FontFamily(
    Font(R.font.comfortaa_medium)
)

val mainColor = "#fcfcfc".toColor()
val btnCityColor = "#85d249".toColor()
val customGrayColor = "#f1f1f1".toColor()
val cardBackGroundColor = "#e1e1e1".toColor()

val API_KEY = "36001eaeaf8d4697a29115746230910"
val citiesList = listOf<String>(
    "London",
    "Moscow",
    "Washington",
    "Kolomna",
)

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainArea(this)
        }
    }
}

@Composable
fun mainArea(context: Context){
    Column(
        modifier = Modifier
            .background(mainColor)
            .padding(10.dp)
            .fillMaxSize()
    ) {
        greetingUser()
        citySelector(context)
    }
}

@Composable
fun greetingUser(){
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
                .padding(top = 30.dp, bottom = 15.dp))
        Box(modifier = Modifier.padding(bottom = 30.dp)) {
            Text(text = "Добро пожаловать!", style = TextStyle(
                fontFamily = comfortaaBold,
                fontSize = 30.sp))
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
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnCityColor,
                    contentColor = Color.White
                ),
                onClick = { menuState.value = !menuState.value }
            ) {
                Text(text = cityState.value, style = TextStyle(
                    fontFamily = comfortaaBold,
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
                    .background(customGrayColor),
            ) {
                citiesList.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item, style = TextStyle(
                                    fontFamily = comfortaaMedium,
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
        weatherForecastArea(cityState, context)
    }
}

@Composable
fun weatherForecastArea(city: MutableState<String>, context: Context) {
    var daysList = remember {
        mutableStateOf(listOf<WeatherModel>())
    }

    var currentDay = remember {
        mutableStateOf(WeatherModel(
            "","","","","","","",
            "","","",""
        ))
    }

    if (city.value == "Выбрать город") defaultWeatherForecastCard()
    else {
        getResult(city, context, daysList, currentDay)
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
fun currentWeatherForecastCard(city: MutableState<String>, context: Context,
                               currentDay: MutableState<WeatherModel>){
    Box(modifier = Modifier
        .fillMaxSize()
        .clip(shape = RoundedCornerShape(10.dp))
        .background(cardBackGroundColor)){
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = customGrayColor)
        ) {
            Row(modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(model = "https:${currentDay.value.icon}",
                    contentDescription = "Weather_icon",
                    modifier = Modifier.size(90.dp))
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "${currentDay.value.currentTemp}°C", style = TextStyle(
                    fontFamily = comfortaaBold,
                    fontSize = 26.sp
                ))
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
fun comfortaaMediumText(text: String){
    Text(text = text, style = TextStyle(
        fontFamily = comfortaaMedium,
        fontSize = 16.sp
    ))
}

@Composable
fun daysWeatherForecast(city: MutableState<String>, context: Context,
                        daysList: MutableState<List<WeatherModel>>) {
    Box(modifier = Modifier
        .padding(start = 5.dp, end = 5.dp)
        .clip(shape = RoundedCornerShape(13.dp))) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardBackGroundColor)
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
fun dayCard(weatherObject: WeatherModel){
    Card(modifier = Modifier
        .padding(top = 5.dp)
        .background(customGrayColor),
        colors = CardDefaults.cardColors(containerColor = customGrayColor)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(model = "https:${weatherObject.icon}", contentDescription = "Weather_Icon",
                    modifier = Modifier.size(50.dp))
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                comfortaaMediumText(text = "${weatherObject.date}")
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                comfortaaMediumText(text = "${weatherObject.condition}")
                comfortaaMediumText(text = "${weatherObject.mintemp_c}°C " +
                        "/ ${weatherObject.maxtemp_c}°C")
            }
        }
    }
}

private fun getResult(city: MutableState<String>, context: Context,
                      daysList: MutableState<List<WeatherModel>>,
                      currentDay: MutableState<WeatherModel>){
    var url = "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=${city.value}&days=5&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val request = StringRequest(
        com.android.volley.Request.Method.GET,
        url,
        {
                response -> var list = parseData(response)
            currentDay.value = list[0]
            daysList.value = list
        },
        {
            Log.d("VolleyLog", "Error: $it")
        }
    )
    queue.add(request)
}

fun parseData(response: String) : List<WeatherModel>{
    if (response == null) return listOf()
    var list = ArrayList<WeatherModel>()

    val mainObject = JSONObject(response)
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
    val current = mainObject.getJSONObject("current")
    val country = mainObject.getJSONObject("location").getString("country")
    val region = mainObject.getJSONObject("location").getString("region")

    for (i in 0 until days.length()){
        var item = days[i] as JSONObject
        list.add(
            WeatherModel(
                country,
                region,
                "",
                "",
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("text"),
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("icon"),
                "",
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONArray("hour").toString(),
                item.getString("date")
            )
        )
    }
    list[0] = list[0].copy(
        feelsLike = current.getString("feelslike_c"),
        currentTemp = current.getString("temp_c"),
        wind_dir = current.getString("wind_dir")
    )
    return list
}