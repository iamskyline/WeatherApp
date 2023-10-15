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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
val customGrayColor = "#e4e4e4".toColor()
val cardBackGroundColor = "#d3d3d3".toColor()

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

fun getResult(city: MutableState<String>, context: Context): WeatherModel?{
    var weatherData: WeatherModel? = null
    var url = "https://api.weatherapi.com/v1/current.json?q=${city.value}&key=$API_KEY"
    val queue = Volley.newRequestQueue(context)
    val request = StringRequest(
        com.android.volley.Request.Method.GET,
        url,
        {
            response -> weatherData = parseCurrentData(response)
            Log.d("WeatherData", "$weatherData")
        },
        {
            Log.d("VolleyLog", "Error: $it")
        }
    )
    queue.add(request)
    return weatherData
}

fun parseCurrentData(response: String) : WeatherModel{
    //if (response == null) return listOf()
    //var listData = ArrayList<WeatherModel>()

    val mainObject = JSONObject(response)

    val current = mainObject.getJSONObject("current")
    var weatherObject = WeatherModel(
        current.getString("temp_c"),
        current.getJSONObject("condition").getString("text"),
        current.getJSONObject("condition").getString("icon"),
        current.getString("wind_dir"),
        "",
        ""
    )
    return weatherObject
}

@Composable
fun weatherForecastArea(city: MutableState<String>, context: Context) {
    if (city.value == "Выбрать город") defaultWeatherForecastCard()
    //else getResult(city, context)
    else {
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
                    currentWeatherForecastCard(city, context)
                }
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .fillParentMaxWidth()
                ) {
                    daysWeatherForecast(city, context)
                }
            }
        }
    }
}

@Composable
fun currentWeatherForecastCard(city: MutableState<String>, context: Context){
    var weatherObject = getResult(city, context)
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
                AsyncImage(model = "https:", contentDescription = "Weather_icon",
                    modifier = Modifier.size(90.dp))
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "30°C", style = TextStyle(
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
                comfortaaMediumText(text = "Погодные условия:")
                comfortaaMediumText(text = "знач.")
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                comfortaaMediumText(text = "Направление ветра:")
                comfortaaMediumText(text = "знач.")
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
fun daysWeatherForecast(city: MutableState<String>, context: Context) {
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
                items(3) {
                    dayCard()
                }
            }
        }
    }
}

@Composable
fun dayCard(){
    Card(modifier = Modifier
        .padding(top = 5.dp)
        .background(customGrayColor),
        colors = CardDefaults.cardColors(containerColor = customGrayColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                comfortaaMediumText(text = "Дата")
                comfortaaMediumText(text = "Состояние")
            }
            AsyncImage(model = "https:", contentDescription = "Weather_Icon",
                modifier = Modifier.size(50.dp))
            comfortaaMediumText(text = "°C")
        }
    }
}

@Composable
fun defaultWeatherForecastCard(){
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
            Image(painter = image, contentDescription = "Нет результатов",
                modifier = Modifier.padding(end = 35.dp, bottom = 20.dp))
            Text(text = "Нечего показывать!", textAlign = TextAlign.Center,
                style = TextStyle(fontFamily = comfortaaMedium, fontSize = 20.sp))
            Text(text = "Для начала, выберите город", style = TextStyle(
                fontFamily = comfortaaMedium, fontSize = 20.sp))
        }
    }
}