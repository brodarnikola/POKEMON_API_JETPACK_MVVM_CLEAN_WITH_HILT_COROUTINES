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

package com.nikola_brodar.data.di_dagger2.youtube

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nikola_brodar.data.BuildConfig
import com.nikola_brodar.data.networking.youtube.YoutubeRepositoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val RETROFIT_BASE_URL = "https://www.googleapis.com/youtube/v3/"

@InstallIn(ApplicationComponent::class)
@Module
class YoutubeNetworkModule {

    @Provides
    @YoutubeNetwork
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

    @Provides
    @Singleton
    @YoutubeNetwork
    fun provideAuthInterceptorOkHttpClient( @YoutubeNetwork interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }


    @Provides
    @Singleton
    @YoutubeNetwork
    fun provideGsonConverterFactory( @YoutubeNetwork gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    @YoutubeNetwork
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    @YoutubeNetwork
    fun provideRetrofit( @YoutubeNetwork converterFactory: GsonConverterFactory, @YoutubeNetwork client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(RETROFIT_BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Singleton
    @Provides
    @YoutubeNetwork
    fun provideYoutubeService( @YoutubeNetwork retrofit: Retrofit.Builder): YoutubeRepositoryApi {
        return retrofit
            .build()
            .create(YoutubeRepositoryApi::class.java)
    }


}
