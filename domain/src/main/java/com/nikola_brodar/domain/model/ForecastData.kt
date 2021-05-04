package com.nikola_brodar.domain.model


import com.google.gson.annotations.SerializedName

data class ForecastData(

    val main: ForecastMain = ForecastMain(),

    val weather: List<ForecastDescription> = listOf(),

    val wind: ForecastWind = ForecastWind(),

    @SerializedName("dt_txt")
    val dateAndTime: String = String()
)
