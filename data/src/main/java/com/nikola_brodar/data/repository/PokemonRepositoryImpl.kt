package com.nikola_brodar.data.repository

import com.nikola_brodar.data.database.PokemonDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.networking.PokemonRepositoryApi
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.repository.PokemonRepository
import com.vjezba.data.lego.api.BaseDataSource

/**
 * RepositoryResponseApi module for handling data operations.
 */

class PokemonRepositoryImpl constructor(
    private val database: PokemonDatabase,
    private val service: PokemonRepositoryApi,
    private val dbMapper: DbMapper?
) : PokemonRepository, BaseDataSource() {

    override suspend fun getAllPokemons(limit: Int, offset: Int): ResultState<*> {
        val result = getResult { service.getAllPokemons(limit, offset) }
        when ( result ) {
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
        val result =  getResult { service.getRandomSelectedPokemon(id) }
        when ( result ) {
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
