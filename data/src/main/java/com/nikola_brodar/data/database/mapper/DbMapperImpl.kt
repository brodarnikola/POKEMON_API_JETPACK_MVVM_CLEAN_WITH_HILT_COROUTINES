package com.nikola_brodar.data.database.mapper

import com.nikola_brodar.data.database.model.DBWeather
import com.nikola_brodar.data.networking.model.*
import com.nikola_brodar.data.networking.youtube.model.ApiAllPokemons
import com.nikola_brodar.data.networking.youtube.model.ApiMainPokemon
import com.nikola_brodar.data.networking.youtube.model.ApiPokemonSprites
import com.nikola_brodar.data.networking.youtube.model.ApiYoutubeVideosMain
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.model.youtube.*

class DbMapperImpl : DbMapper {

    override fun mapApiWeatherToDomainWeather(apiWeather: ApiWeather): ResultState<Weather> {
        return with(apiWeather) {
            ResultState.Success(
                Weather(
                    weather.map {
                        WeatherData(it.description)
                    },
                    WeatherMain(
                        main.temp,
                        main.feelsLike,
                        main.tempMax,
                        main.tempMin,
                        main.humidity
                    ),
                    WeatherWind(wind.speed),
                    name
                )
            )
        }
    }

    override fun mapApiForecastToDomainForecast(apiForecast: ApiForecast): ResultState<Forecast> {
        return with(apiForecast) {
            ResultState.Success(
                Forecast(
                    code,
                    forecastList.map {
                        with(it) {
                            ForecastData(
                                ForecastMain(it.main.temp, it.main.feelsLike, it.main.tempMax),
                                it.weather.map {
                                    ForecastDescription(it.description)
                                },
                                ForecastWind(it.wind.speed),
                                dateAndTime
                            )
                        }
                    },
                    CityData(cityData.country, cityData.population)
                )
            )
        }
    }

    override fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBWeather> {
        return forecast.forecastList.map {
            val descriptionData = if (it.weather.isNotEmpty())
                it.weather[0].description
            else
                ""
            DBWeather(
                it.main.temp,
                it.main.feelsLike,
                it.main.tempMax,
                descriptionData,
                it.wind.speed,
                it.dateAndTime
            )
        }
    }

    override fun mapDBWeatherListToWeather(weather: DBWeather): ForecastData {
        return with(weather) {
            ForecastData(
                ForecastMain(weather.temp, weather.feelsLike, weather.tempMax),
                listOf(ForecastDescription(weather.description)),
                ForecastWind(weather.speed),
                dateAndTime
            )
        }
    }

    override fun mapApiYoutubeVideosToDomainYoutube(youtubeVideosMain: ApiYoutubeVideosMain): YoutubeVideosMain {

        return with(youtubeVideosMain) {
            YoutubeVideosMain(
                nextPageToken,
                regionCode,
                items.map {

                    YoutubeVideos(
                        YoutubeVideoId(it.id.kind, it.id.videoId),
                        YoutubeVideoSnippet(
                            it.snippet.title, it.snippet.description,
                            YoutubeVideoThumbnails(
                                YoutubeVideoThumbnailsMedium(
                                    it.snippet.thumbnails.medium.url,
                                    it.snippet.thumbnails.medium.width,
                                    it.snippet.thumbnails.medium.height
                                )
                            )
                        )
                    )

                }
            )
        }

    }

    override fun mapAllPokemonToDomainAllPokemon(pokemon: ApiAllPokemons): AllPokemons {
        return with(pokemon) {
            AllPokemons(
                count,
                results.map {
                    AllPokemonsData(
                        it.name,
                        it.url
                    )
                }
            )
        }
    }

    override fun mapApiPokemonToDomainPokemon(pokemon: ApiMainPokemon): MainPokemon {
        return with(pokemon) {
            MainPokemon(
                name,
                PokemonSprites(
                    sprites.backDefault,
                    sprites.frontDefault
                ),
                stats.map {
                    with(it) {
                        PokemonStats(
                            baseStat,
                            stat = PokemonStatsName(
                                stat.name
                            )
                        )
                    }
                },
                forms.map {
                    with(it) {
                        PokemonForms(
                            name
                        )
                    }
                },
                baseExperience,
                weight
            )
        }
    }

//    override fun mapApiPokemonToDomainPokemon(pokemon: ApiMainPokemon): ResultState<MainPokemon> {
//        return with(pokemon) {
//            ResultState.Success(
//                MainPokemon(
//                    name,
//                    PokemonSprites(
//                        sprites.backDefault,
//                        sprites.frontDefault
//                    ),
//                    stats.map {
//                        with(it) {
//                            PokemonStats(
//                                baseStat,
//                                stat = PokemonStatsName(
//                                    stat.name
//                                )
//                            )
//                        }
//                    },
//                    forms.map {
//                        with(it) {
//                            PokemonForms(
//                                name
//                            )
//                        }
//                    },
//                    baseExperience,
//                    weight
//                )
//            )
//        }
//    }


}
