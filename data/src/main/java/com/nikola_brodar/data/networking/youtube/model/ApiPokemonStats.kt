package com.nikola_brodar.data.networking.youtube.model

import com.google.gson.annotations.SerializedName

data class ApiPokemonStats (

    @SerializedName("base_stat")
    val baseStat: Int = 0,

    val stat: ApiPokemonStatsName = ApiPokemonStatsName()

)