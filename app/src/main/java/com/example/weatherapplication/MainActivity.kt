package com.example.weatherapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapplication.components.CustomColors
import com.example.weatherapplication.components.Fonts
import com.example.weatherapplication.components.ForecastComponents
import com.example.weatherapplication.models.WeatherModel
import com.example.weatherapplication.components.GeneralMethods
import com.example.weatherapplication.components.GreetingComponents

val methods = GeneralMethods()
val customColors = CustomColors()
var greetingComponents = GreetingComponents()
var fonts = Fonts()

val API_KEY = "36001eaeaf8d4697a29115746230910"
val citiesList = listOf<String>(
    "London",
    "Moscow",
    "Washington",
    "Kolomna",
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainArea(this)
        }
    }
}

@Composable
fun mainArea(context: Context) {
    Column(
        modifier = Modifier
            .background(customColors.mainColor)
            .padding(10.dp)
            .fillMaxSize()
    ) {
        greetingComponents.greetingUser()
        greetingComponents.citySelector(context)
    }
}

@Composable
fun comfortaaMediumText(text: String) {
    Text(
        text = text, style = TextStyle(
            fontFamily = fonts.comfortaaMedium,
            fontSize = 16.sp
        )
    )
}