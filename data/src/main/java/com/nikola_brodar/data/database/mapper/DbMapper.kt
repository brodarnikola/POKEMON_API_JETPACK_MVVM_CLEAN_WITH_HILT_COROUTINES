package com.nikola_brodar.data.database.mapper

import com.nikola_brodar.data.database.model.DBWeather
import com.nikola_brodar.data.networking.model.*
import com.nikola_brodar.data.networking.youtube.model.ApiMainPokemon
import com.nikola_brodar.data.networking.youtube.model.ApiYoutubeVideosMain
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.model.youtube.YoutubeVideosMain


interface DbMapper {

    // forecast
    fun mapApiWeatherToDomainWeather(apiForecast: ApiWeather): ResultState<Weather>

    fun mapApiForecastToDomainForecast(apiForecast: ApiForecast): ResultState<Forecast>

    fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBWeather>

    fun mapDBWeatherListToWeather(weather: DBWeather): ForecastData


    fun mapApiYoutubeVideosToDomainYoutube( youtubeVideosMain: ApiYoutubeVideosMain): YoutubeVideosMain


    fun mapApiPokemonToDomainPokemon( pokemon: ApiMainPokemon): ResultState<MainPokemon>
}