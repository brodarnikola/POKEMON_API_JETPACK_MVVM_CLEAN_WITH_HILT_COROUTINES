package com.nikola_brodar.domain.model

import com.google.gson.annotations.SerializedName

data class MainPokemon (


    val name: String = "",

    val sprites: PokemonSprites = PokemonSprites(),

    val stats: List<PokemonStats> = listOf(),

    val moves: List<PokemonMainMoves> = listOf(),

)