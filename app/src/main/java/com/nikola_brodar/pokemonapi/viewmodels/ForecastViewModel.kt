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

import android.content.ContentValues
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikola_brodar.data.database.WeatherDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.di_dagger2.WeatherNetwork
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.CityData
import com.nikola_brodar.domain.model.Forecast
import com.nikola_brodar.domain.model.ForecastData
import com.nikola_brodar.domain.model.MainPokemon
import com.nikola_brodar.domain.repository.WeatherRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ForecastViewModel @ViewModelInject constructor(
    @WeatherNetwork private val weatherRepository: WeatherRepository,
    private val dbWeather: WeatherDatabase,
    private val dbMapper: DbMapper?
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

//    private val _forecastMutableLiveData = MutableLiveData<Forecast>().apply {
//        value = Forecast("", listOf(), CityData("", 0L))
//    }
//
//    val forecastList: LiveData<Forecast> = _forecastMutableLiveData

    private val _forecastMutableLiveData: MutableLiveData<ResultState<*>> = MutableLiveData()

    val forecastList:  LiveData<ResultState<*>> = _forecastMutableLiveData

    fun getPokemonData(id: Int) {
        weatherRepository.getPokemonData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<MainPokemon> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(responseForecast: MainPokemon) {


                    Log.d(
                        ContentValues.TAG,
                        "onNext received: " + responseForecast.weight + " baseExperince" + responseForecast.baseExperience
                    )
                    //insertWeatherIntoDatabase(responseForecast)

                    //_forecastMutableLiveData.value = responseForecast
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        ContentValues.TAG,
                        "onError received: " + e.message
                    )
                }

                override fun onComplete() {

                }
            })
    }

    fun getForecastFromNetwork(cityName: String) {
        weatherRepository.getForecastData(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<ResultState<*>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(responseForecast: ResultState<*>) {

                    insertWeatherIntoDatabase(responseForecast)

                    _forecastMutableLiveData.value = responseForecast
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        ContentValues.TAG,
                        "onError received: " + e.message
                    )
                }

                override fun onComplete() {

                }
            })
    }

    private fun insertWeatherIntoDatabase(response: ResultState<*>) {
        when ( response ) {
            is ResultState.Success -> {
                Observable.fromCallable {

                    val responseForecast = response.data as Forecast

                    val weather = dbMapper?.mapDomainWeatherToDbWeather(responseForecast) ?: listOf()
                    dbWeather.weatherDAO().updateWeather(
                        weather
                    )
                    Log.d(
                        "da li ce uci unutra * ",
                        "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString()
                    )
                }
                    .doOnError { Log.e("Error in observables", "Error is: ${it.message}, ${throw it}") }
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
                Log.d(ContentValues.TAG, "Exception inside forecastViewModel is: ${   exceptionForecast}")
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
                _forecastMutableLiveData.value = forecastData
            }
            .subscribe()
    }

    private fun getForecastFromDB(): List<ForecastData> {
        return dbWeather.weatherDAO().getWeather().map {
            dbMapper?.mapDBWeatherListToWeather(it) ?: ForecastData()
        }
    }


    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}

