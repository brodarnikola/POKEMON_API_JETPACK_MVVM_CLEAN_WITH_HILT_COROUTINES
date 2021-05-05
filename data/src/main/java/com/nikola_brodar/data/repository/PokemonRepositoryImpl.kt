package com.nikola_brodar.data.repository

import com.nikola_brodar.data.database.PokemonDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.networking.PokemonRepositoryApi
import com.nikola_brodar.domain.model.*
import com.nikola_brodar.domain.repository.PokemonRepository

/**
 * RepositoryResponseApi module for handling data operations.
 */

class PokemonRepositoryImpl constructor(
    private val database: PokemonDatabase,
    private val service: PokemonRepositoryApi,
    private val dbMapper: DbMapper?
) : PokemonRepository {

    override suspend fun getAllPokemons(limit: Int, offset: Int): AllPokemons {
        val result = service.getAllPokemons(limit, offset)
        val correctResult = dbMapper?.mapAllPokemonToDomainAllPokemon(result.body()!!)!!
        return correctResult
    }

    override suspend fun getRandomSelectedPokemon(id: Int): MainPokemon {
        val result = service.getRandomSelectedPokemon(id)
        val correctResult = dbMapper?.mapApiPokemonToDomainPokemon(result.body()!!)!!
        return correctResult
    }

}
