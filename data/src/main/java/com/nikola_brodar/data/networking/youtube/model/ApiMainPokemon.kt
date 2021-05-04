package com.nikola_brodar.data.networking.youtube.model

import com.google.gson.annotations.SerializedName

data class ApiMainPokemon (

    val name: String = "",

    val sprites: ApiPokemonSprites = ApiPokemonSprites(),

    val stats: List<ApiPokemonStats> = listOf(),

    val forms: List<ApiPokemonForms> = listOf(),

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