package com.nikola_brodar.data.networking.model


import com.google.gson.annotations.SerializedName

data class ApiForecast(

    val code: String = "",

    @SerializedName("list")
    val forecastList: List<ApiForecastData> = listOf(),
    @SerializedName("city")
    val cityData: ApiCityData = ApiCityData(),
)
