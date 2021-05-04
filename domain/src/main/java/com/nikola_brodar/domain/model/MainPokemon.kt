package com.nikola_brodar.domain.model

import com.google.gson.annotations.SerializedName

data class MainPokemon (


    val name: String = "",

    val sprites: PokemonSprites = PokemonSprites(),

    val forms: List<PokemonForms> = listOf(),

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