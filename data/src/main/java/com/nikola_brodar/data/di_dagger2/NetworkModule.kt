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

package com.nikola_brodar.data.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nikola_brodar.data.BuildConfig
import com.nikola_brodar.data.di_dagger2.PokemonNetwork
import com.nikola_brodar.data.networking.WeatherRepositoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



private const val RETROFIT_BASE_URL = "https://pokeapi.co/api/v2/"

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModuleHilt {


    @Provides
    @PokemonNetwork
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

    @Provides
    @Singleton
    @PokemonNetwork
    fun provideAuthInterceptorOkHttpClient( @PokemonNetwork interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }


    @Provides
    @Singleton
    @PokemonNetwork
    fun provideGsonConverterFactory( @PokemonNetwork gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    @PokemonNetwork
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    @PokemonNetwork
    fun provideRetrofit(@PokemonNetwork converterFactory: GsonConverterFactory, @PokemonNetwork client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(RETROFIT_BASE_URL)
            .addConverterFactory(converterFactory)
    }

    @Singleton
    @Provides
    @PokemonNetwork
    fun provideWeatherService( @PokemonNetwork retrofit: Retrofit.Builder): WeatherRepositoryApi {
        return retrofit
            .build()
            .create(WeatherRepositoryApi::class.java)
    }

}
