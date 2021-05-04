package com.nikola_brodar.domain.model


import com.google.gson.annotations.SerializedName

data class ForecastMain(

    var temp: String = "",

    @SerializedName("feels_like")
    val feelsLike: String = "",

    @SerializedName("temp_max")
    val tempMax: String = ""
)
