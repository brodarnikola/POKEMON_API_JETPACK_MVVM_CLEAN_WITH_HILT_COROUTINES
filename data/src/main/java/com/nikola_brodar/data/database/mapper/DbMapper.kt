package com.nikola_brodar.data.database.mapper

import com.nikola_brodar.data.database.model.DBMainPokemon
import com.nikola_brodar.data.database.model.DBPokemon
import com.nikola_brodar.data.database.model.DBPokemonMoves
import com.nikola_brodar.data.database.model.DBPokemonStats
import com.nikola_brodar.data.networking.model.*
import com.nikola_brodar.data.networking.youtube.model.ApiAllPokemons
import com.nikola_brodar.data.networking.youtube.model.ApiMainPokemon
import com.nikola_brodar.data.networking.youtube.model.ApiYoutubeVideosMain
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.model.youtube.YoutubeVideosMain


interface DbMapper {

    // forecast
    fun mapApiWeatherToDomainWeather(apiForecast: ApiWeather): ResultState<Weather>

    fun mapApiForecastToDomainForecast(apiForecast: ApiForecast): ResultState<Forecast>

    fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBPokemon>

    fun mapDBWeatherListToWeather(pokemon: DBPokemon): ForecastData


    fun mapApiYoutubeVideosToDomainYoutube( youtubeVideosMain: ApiYoutubeVideosMain): YoutubeVideosMain




    fun mapAllPokemonToDomainAllPokemon( pokemon: ApiAllPokemons): AllPokemons

    fun mapApiPokemonToDomainPokemon( pokemon: ApiMainPokemon): MainPokemon

    fun mapDomainMainPokemonToDBMainPokemon(pokemon: MainPokemon): DBMainPokemon

    fun mapDomainPokemonStatsToDbPokemonStats( pokemon: List<PokemonStats>): List<DBPokemonStats>

    fun mapDomainPokemonMovesToDbPokemonMoves( pokemon: List<PokemonMainMoves>): List<DBPokemonMoves>

    //fun mapApiPokemonToDomainPokemon( pokemon: ApiMainPokemon): ResultState<MainPokemon>
}