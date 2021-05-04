package com.nikola_brodar.data.networking.youtube.model

import com.google.gson.annotations.SerializedName

data class ApiPokemonSprites (

    @SerializedName("back_default")
    val backDefault: String = "",

    @SerializedName("front_default")
    val frontDefault: String = ""
)