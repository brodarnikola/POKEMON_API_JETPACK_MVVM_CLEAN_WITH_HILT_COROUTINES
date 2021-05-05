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

    override fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBPokemon> {
        return forecast.forecastList.map {
            val descriptionData = if (it.weather.isNotEmpty())
                it.weather[0].description
            else
                ""
            DBPokemon(
                it.main.temp,
                it.main.feelsLike,
                it.main.tempMax,
                descriptionData,
                it.wind.speed,
                it.dateAndTime
            )
        }
    }



    override fun mapDomainPokemonStatsToDbPokemonStats(pokemon: List<PokemonStats>): List<DBPokemonStats> {
        return pokemon.map {
            DBPokemonStats(
                it.baseStat,
                it.stat.name
            )
        }
    }

    override fun mapDomainPokemonMovesToDbPokemonMoves(pokemon: List<PokemonMainMoves>): List<DBPokemonMoves> {
        return pokemon.map {
            DBPokemonMoves(
                it.move.name,
                it.move.url
            )
        }
    }

    override fun mapDBWeatherListToWeather(pokemon: DBPokemon): ForecastData {
        return with(pokemon) {
            ForecastData(
                ForecastMain(pokemon.temp, pokemon.feelsLike, pokemon.tempMax),
                listOf(ForecastDescription(pokemon.description)),
                ForecastWind(pokemon.speed),
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
                moves.map {
                    PokemonMainMoves(
                        PokemonMove(
                            it.move.name,
                            it.move.url
                        )
                    )
                }
            )
        }
    }

    override fun mapDomainMainPokemonToDBMainPokemon(pokemon: MainPokemon): DBMainPokemon {
        return with(pokemon) {
            DBMainPokemon(
                name,
                backDefault = sprites.backDefault,
                frontDefault = sprites.frontDefault
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
