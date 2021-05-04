package com.nikola_brodar.data.networking.model



data class ApiWeather(

    val weather: List<ApiWeatherData> = listOf(),
    val main: ApiWeatherMain = ApiWeatherMain(),
    val wind: ApiWeatherWind = ApiWeatherWind(),
    val name: String = ""
)
