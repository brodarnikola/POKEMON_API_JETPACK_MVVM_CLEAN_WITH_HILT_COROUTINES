package com.nikola_brodar.data.networking.model


import com.google.gson.annotations.SerializedName

data class ApiForecastData(

    val main: ApiForecastMain = ApiForecastMain(),

    val weather: List<ApiForecastDescription> = listOf(),

    val wind: ApiForecastWind = ApiForecastWind(),

    @SerializedName("dt_txt")
    val dateAndTime: String = String()
)
