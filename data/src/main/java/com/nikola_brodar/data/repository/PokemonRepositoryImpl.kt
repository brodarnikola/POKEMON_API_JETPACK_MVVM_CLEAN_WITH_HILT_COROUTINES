package com.nikola_brodar.data.repository

import android.content.ContentValues
import android.util.Log
import com.nikola_brodar.data.database.PokemonDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.networking.PokemonRepositoryApi
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.repository.PokemonRepository
import com.vjezba.data.lego.api.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * RepositoryResponseApi module for handling data operations.
 */

class PokemonRepositoryImpl constructor(
    private val database: PokemonDatabase,
    private val service: PokemonRepositoryApi,
    private val dbMapper: DbMapper?
) : PokemonRepository, BaseDataSource() {

    override fun getAllPokemonsNewFlow(limit: Int, offset: Int): Flow<ResultState<*>> =
        flow {
            val result = getResult { service.getAllPokemons(limit, offset) }
            when (result) {
                is ResultState.Success -> {
                    val correctResult =
                        dbMapper?.mapAllPokemonToDomainAllPokemon(result.data) ?: AllPokemons()

                    val pokemonID = getRandomSelectedPokemonId(correctResult)
                    val randomPokemonResult =
                        getResult { service.getRandomSelectedPokemon(pokemonID) }

                    when (randomPokemonResult) {
                        is ResultState.Success -> {

                            val correctResult = dbMapper?.mapApiPokemonToDomainPokemon(randomPokemonResult.data)
                            System.out.println("Will it enter here${correctResult}")
                            emit((ResultState.Success(correctResult)))
//                                    deleteAllPokemonData()
//                                    insertPokemonIntoDatabase(randomSelectedPokemon.data as MainPokemon)
//                                    _pokemonMutableLiveData.value = randomSelectedPokemon
                        }
                        is ResultState.Error -> {
                            emit(
                                ResultState.Error(
                                    randomPokemonResult.message,
                                    randomPokemonResult.exception
                                )
                            )
                            //  _pokemonMutableLiveData.value = randomSelectedPokemon
                        }
                    }

                    // .collect()

                    // .collect()


                    // .launchIn(viewModelScope)

                    // emit((ResultState.Success(correctResult)))

                    // return ResultState.Success(correctResult)
                }
                is ResultState.Error -> {
                    emit(ResultState.Error(result.message, result.exception))
                    // return ResultState.Error(result.message, result.exception)
                }
                else -> {
                    emit(ResultState.Error("", null))
                }
            }
        }

    private fun getRandomSelectedPokemonId(allPokemons: AllPokemons): Int {
        val separateString = allPokemons.results.random().url.split("/")
        val pokemonId = separateString.get(separateString.size - 2)
        Log.d(
            ContentValues.TAG,
            "Id is: ${pokemonId.toInt()}"
        )
        return pokemonId.toInt()
    }


    override suspend fun getAllPokemons(limit: Int, offset: Int): ResultState<*> {
        val result = getResult { service.getAllPokemons(limit, offset) }
        when (result) {
            is ResultState.Success -> {
                val correctResult = dbMapper?.mapAllPokemonToDomainAllPokemon(result.data)
                return ResultState.Success(correctResult)
            }
            is ResultState.Error -> {
                return ResultState.Error(result.message, result.exception)
            }
            else -> {
                return ResultState.Error("", null)
            }
        }
    }

    override suspend fun getRandomSelectedPokemon(id: Int): ResultState<*> { // MainPokemon {
        val result = getResult { service.getRandomSelectedPokemon(id) }
        when (result) {
            is ResultState.Success -> {
                val correctResult = dbMapper?.mapApiPokemonToDomainPokemon(result.data)
                return ResultState.Success(correctResult)
            }
            is ResultState.Error -> {
                return ResultState.Error(result.message, result.exception)
            }
            else -> {
                return ResultState.Error("", null)
            }
        }
    }

}
