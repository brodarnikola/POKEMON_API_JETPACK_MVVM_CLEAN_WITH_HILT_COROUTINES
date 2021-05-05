package com.nikola_brodar.data.networking.youtube.model

import com.google.gson.annotations.SerializedName

data class ApiMainPokemon (

    val name: String = "",

    val sprites: ApiPokemonSprites = ApiPokemonSprites(),

    val stats: List<ApiPokemonStats> = listOf(),

    val moves: List<ApiPokemonMainMoves> = listOf(),
)