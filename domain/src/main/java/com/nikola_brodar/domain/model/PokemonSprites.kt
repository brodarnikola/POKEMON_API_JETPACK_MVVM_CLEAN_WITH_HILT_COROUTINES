package com.nikola_brodar.domain.model

import com.google.gson.annotations.SerializedName


data class PokemonSprites (
    @SerializedName("back_default")
    val backDefault: String = "",
    @SerializedName("front_default")
    val frontDefault: String = ""
)