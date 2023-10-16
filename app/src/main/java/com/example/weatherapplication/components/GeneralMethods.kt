package com.example.weatherapplication.components

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapplication.API_KEY
import com.example.weatherapplication.models.WeatherModel
import org.json.JSONObject

public class GeneralMethods {
    public fun getResult(
        city: MutableState<String>, context: Context,
        daysList: MutableState<List<WeatherModel>>,
        currentDay: MutableState<WeatherModel>
    ) {
        var url =
            "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=${city.value}&days=5&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                var list = parseData(response)
                currentDay.value = list[0]
                daysList.value = list
            },
            {
                Log.d("VolleyLog", "Error: $it")
            }
        )
        queue.add(request)
    }

    public fun parseData(response: String): List<WeatherModel> {
        if (response == null) return listOf()
        var list = ArrayList<WeatherModel>()

        val mainObject = JSONObject(response)
        val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val current = mainObject.getJSONObject("current")
        val country = mainObject.getJSONObject("location").getString("country")
        val region = mainObject.getJSONObject("location").getString("region")

        for (i in 0 until days.length()) {
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
}