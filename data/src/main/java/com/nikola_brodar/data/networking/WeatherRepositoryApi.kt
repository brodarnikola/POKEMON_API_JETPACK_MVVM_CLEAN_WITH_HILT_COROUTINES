/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nikola_brodar.data.networking


import com.nikola_brodar.data.networking.model.*
import com.nikola_brodar.data.networking.youtube.model.ApiAllPokemons
import com.nikola_brodar.data.networking.youtube.model.ApiMainPokemon
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherRepositoryApi {

    // forecast api key ->   b389e4ccf5ae4bbc8072ccd05c8f85c7


//    @GET("weather")
//    @Headers("Content-Type: application/json")
//    fun getWeather( @Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appId") appId: String ): Flowable<ApiWeather>
//
//    @GET("weather")
//    @Headers("Content-Type: application/json")
//    fun getWeatherByCityName( @Query("q") cityName: String, @Query("appId") appId: String ): Flowable<ApiWeather>
//
//    @GET("forecast")
//    @Headers("Content-Type: application/json")
//    fun getForecast( @Query("q") cityName: String, @Query("appId") appId: String ): Flowable<ApiForecast>



    @GET("pokemon")
    @Headers("Content-Type: application/json")
    suspend fun getAllPokemons(  @Query("limit") limit: Int, @Query("offset") offset: Int  ): Response<ApiAllPokemons>



    @GET("pokemon/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getRandomSelectedPokemon( @Path("id") id: Int ):  Response<ApiMainPokemon>


}
