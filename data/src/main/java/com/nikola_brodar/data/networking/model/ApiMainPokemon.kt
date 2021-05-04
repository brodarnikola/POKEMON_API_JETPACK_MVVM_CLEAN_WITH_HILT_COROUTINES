package com.nikola_brodar.data.networking.model

import com.google.gson.annotations.SerializedName

data class ApiMainPokemon (


    @SerializedName("base_experience")
    val baseExperience: Int = 0,

    @SerializedName("weight")
    val weight: Int = 0,


//    val code: String = "",
//
//    @SerializedName("list")
//    val forecastList: List<ApiForecastData> = listOf(),
//    @SerializedName("city")
//    val cityData: ApiCityData = ApiCityData(),

)