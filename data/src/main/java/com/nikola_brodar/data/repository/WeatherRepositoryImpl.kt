

package com.nikola_brodar.data.repository

import com.nikola_brodar.data.API_KEY_FOR_OPEN_WEATHER
import com.nikola_brodar.data.database.WeatherDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.networking.WeatherRepositoryApi
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.repository.WeatherRepository
import io.reactivex.Flowable
import java.net.URLEncoder

/**
 * RepositoryResponseApi module for handling data operations.
 */

class WeatherRepositoryImpl constructor(
    private val database: WeatherDatabase,
    private val service: WeatherRepositoryApi,
    private val dbMapper: DbMapper?
) : WeatherRepository {

    override fun getWeatherData(latitude: Double, longitude: Double): Flowable<ResultState<Weather>> {

        val result = service.getWeather(latitude, longitude, API_KEY_FOR_OPEN_WEATHER)

        //Observable.concatArrayEager(newsResult, observableFromDB)

        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }

        return correctResult
    }

    override fun getWeatherDataByCityName(cityName: String): Flowable<ResultState<Weather>> {

        val cityNameUrlEncoded = URLEncoder.encode(cityName, "utf-8")

        val result = service.getWeatherByCityName(cityNameUrlEncoded, API_KEY_FOR_OPEN_WEATHER)
        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }

        return correctResult
    }

    override fun getForecastData(cityName: String): Flowable<ResultState<Forecast>> {
        val result = service.getForecast(cityName, API_KEY_FOR_OPEN_WEATHER)

        val correctResult = result.map { dbMapper?.mapApiForecastToDomainForecast(it)!! }

        return correctResult
    }

    override fun getPokemonData(id: Int): Flowable<ResultState<MainPokemon>> {
        val result = service.getPokemonData(id)
        val correctResult = result.map { dbMapper?.mapApiPokemonToDomainPokemon(it)!! }
        return correctResult
    }

}
