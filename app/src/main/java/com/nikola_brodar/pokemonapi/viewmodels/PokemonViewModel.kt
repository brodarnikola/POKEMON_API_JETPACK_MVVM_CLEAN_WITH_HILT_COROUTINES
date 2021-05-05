/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nikola_brodar.pokemonapi.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola_brodar.data.database.PokemonDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.database.model.DBMainPokemon
import com.nikola_brodar.data.database.model.DBPokemonStats
import com.nikola_brodar.data.di_dagger2.WeatherNetwork
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.repository.PokemonRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


class PokemonViewModel @ViewModelInject constructor(
    @WeatherNetwork private val pokemonRepository: PokemonRepository,
    private val dbPokemon: PokemonDatabase,
    private val dbMapper: DbMapper?
) : ViewModel() {

    private val _pokemonMutableLiveData: MutableLiveData<MainPokemon> = MutableLiveData()

    val mainPokemonData: LiveData<MainPokemon> = _pokemonMutableLiveData

    @SuppressLint("CheckResult")
    fun getPokemonData(id: Int) {

        viewModelScope.launch {
            val allPokemonsData =
                getAllPokemonData()

            val randomPokemonUrl = allPokemonsData.results.random().url.split("/")
            val id = randomPokemonUrl.get( randomPokemonUrl.size - 2 )
            Log.d(
                ContentValues.TAG,
                "last id is: ${id.toInt()}"
            )
            val pokemonData = pokemonRepository.getRandomSelectedPokemon(id.toInt())
            insertPokemonIntoDatabase(pokemonData)
            _pokemonMutableLiveData.value = pokemonData
        }
    }

    private suspend fun insertPokemonIntoDatabase(pokemonData: MainPokemon) {
        val responseForecast = pokemonData

        val pokemonMain =
            dbMapper?.mapDomainMainPokemonToDBMainPokemon(pokemonData) ?: DBMainPokemon()
        dbPokemon.pokemonDAO().insertMainPokemonData(pokemonMain)



        val pokemonStats =
            dbMapper?.mapDomainPokemonStatsToDbPokemonStats(pokemonData.stats) ?: listOf()
        dbPokemon.pokemonDAO().insertStatsPokemonData(pokemonStats)

        val pokemonMoves =
            dbMapper?.mapDomainPokemonMovesToDbPokemonMoves(pokemonData.moves) ?: listOf()
        dbPokemon.pokemonDAO().insertMovesPokemonData(pokemonMoves)


        val test5 = dbPokemon.pokemonDAO().getSelectedMainPokemonData()
        val test7 = dbPokemon.pokemonDAO().getSelectedStatsPokemonData()
        val test9 = dbPokemon.pokemonDAO().getSelectedMovesPokemonData()


//        val weather =
//            dbMapper?.mapDomainWeatherToDbWeather(responseForecast) ?: listOf()
//        dbPokemon.pokemonDAO().updateWeather(
//            weather
//        )
        Log.d(
            "da li ce uci unutra * ",
            "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString()
        )
    }

    private suspend fun getAllPokemonData(): AllPokemons {
        return pokemonRepository.getAllPokemons(100, 0)
    }

    fun getForecastFromNetwork(cityName: String) {
    }

    private fun insertWeatherIntoDatabase(response: ResultState<*>) {
        when (response) {
            is ResultState.Success -> {
                Observable.fromCallable {

                    val responseForecast = response.data as Forecast

                    val weather =
                        dbMapper?.mapDomainWeatherToDbWeather(responseForecast) ?: listOf()
                    dbPokemon.pokemonDAO().updateWeather(
                        weather
                    )
                    Log.d(
                        "da li ce uci unutra * ",
                        "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString()
                    )
                }
                    .doOnError {
                        Log.e(
                            "Error in observables",
                            "Error is: ${it.message}, ${throw it}"
                        )
                    }
                    .subscribeOn(Schedulers.io())
                    .subscribe {

                        val responseForecast = response.data as Forecast
                        Log.d(
                            "Hoce spremiti vijesti",
                            "Inserted ${responseForecast.forecastList.size} forecast data from API into DB..."
                        )
                    }
            }
            is ResultState.Error -> {
                val exceptionForecast = response.exception
                Log.d(
                    ContentValues.TAG,
                    "Exception inside pokemonViewModel is: ${exceptionForecast}"
                )
            }
        }

    }

    fun getWeatherFromLocalStorage() {
        Observable.fromCallable {
            val listForecast = getForecastFromDB()
            val responseForecast = Forecast("", listForecast, CityData())
            ResultState.Success(responseForecast)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { forecastData: ResultState.Success<Forecast> ->
                Log.i("Size of database", "Size when reading database is: ${forecastData}")
                //_pokemonMutableLiveData.value = forecastData
            }
            .subscribe()
    }

    private fun getForecastFromDB(): List<ForecastData> {
        return dbPokemon.pokemonDAO().getWeather().map {
            dbMapper?.mapDBWeatherListToWeather(it) ?: ForecastData()
        }
    }


}

