package com.example.weatherapplication.models

data class WeatherModel(
    val country: String,
    val region: String,
    val feelsLike: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val wind_dir: String,
    val mintemp_c: String,
    val maxtemp_c: String,
    val hours: String,
    val date: String
)
