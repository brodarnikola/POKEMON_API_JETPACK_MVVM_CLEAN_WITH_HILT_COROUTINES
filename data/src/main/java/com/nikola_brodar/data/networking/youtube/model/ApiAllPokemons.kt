package com.nikola_brodar.data.networking.youtube.model

import com.google.gson.annotations.SerializedName

data class ApiAllPokemons (

    val count: Int = 0,

    val results: List<ApiAllPokemonsData> = listOf(),
)