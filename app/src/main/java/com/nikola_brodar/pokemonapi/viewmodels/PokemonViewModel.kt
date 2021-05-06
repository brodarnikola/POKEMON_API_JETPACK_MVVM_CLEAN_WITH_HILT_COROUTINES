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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola_brodar.data.database.PokemonDatabase
import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.database.model.DBMainPokemon
import com.nikola_brodar.data.database.model.DBPokemonMoves
import com.nikola_brodar.data.di_dagger2.PokemonNetwork
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.AllPokemons
import com.nikola_brodar.domain.model.MainPokemon
import com.nikola_brodar.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    @PokemonNetwork private val pokemonRepository: PokemonRepository,
    private val dbPokemon: PokemonDatabase,
    private val dbMapper: DbMapper?
) : ViewModel() {

    private val _pokemonMutableLiveData: MutableLiveData<ResultState<*>> = MutableLiveData()

    val mainPokemonData: LiveData<ResultState<*>> = _pokemonMutableLiveData

    private val _pokemonMovesMutableLiveData: MutableLiveData<List<DBPokemonMoves>> = MutableLiveData()

    val pokemonMovesData: LiveData<List<DBPokemonMoves>> = _pokemonMovesMutableLiveData

    fun getPokemonMovesFromLocalStorage() {
        viewModelScope.launch {
            val listPokemonMove = getPokemonMovesFromDB()
            _pokemonMovesMutableLiveData.value = listPokemonMove
        }
    }

    private suspend fun getPokemonMovesFromDB(): List<DBPokemonMoves> {
        return dbPokemon.pokemonDAO().getSelectedMovesPokemonData()
    }

    fun getAllPokemonDataFromLocalStorage() {
        viewModelScope.launch {
            val mainPokemonData = getAllPokemonDataFromRoom()
            val successPokemonData = ResultState.Success(mainPokemonData)
            _pokemonMutableLiveData.value = successPokemonData
        }
    }

    private suspend fun getAllPokemonDataFromRoom(): MainPokemon {
        val pokemonMain = dbPokemon.pokemonDAO().getSelectedMainPokemonData()
        val pokemonStats = dbPokemon.pokemonDAO().getSelectedStatsPokemonData()
        val pokemonMoves = dbPokemon.pokemonDAO().getSelectedMovesPokemonData()

        val correctPokemonMain = MainPokemon()
        correctPokemonMain.name = pokemonMain.name
        correctPokemonMain.sprites.backDefault = pokemonMain.backDefault
        correctPokemonMain.sprites.frontDefault = pokemonMain.frontDefault

        correctPokemonMain.stats = dbMapper?.mapDbPokemonStatsToDbPokemonStats(pokemonStats) ?: listOf()
        correctPokemonMain.moves = dbMapper?.mapDbPokemonMovesToDbPokemonMoves(pokemonMoves) ?: listOf()

        return correctPokemonMain
    }

    fun getPokemonData() {

        viewModelScope.launch {

            flowOf( getAllPokemonData() )
                .onEach {  allPokemons ->
                    when( allPokemons ) {
                        is ResultState.Success -> {
                            val randomPokemonUrl = allPokemons.data as AllPokemons
                            val separateString = randomPokemonUrl.results.random().url.split("/")
                            val pokemonId = separateString.get( separateString.size - 2 )
                            Log.d(
                                ContentValues.TAG,
                                "Id is: ${pokemonId.toInt()}"
                            )
                            flowOf( pokemonRepository.getRandomSelectedPokemon(pokemonId.toInt()) )
                                .map { randomSelectedPokemon ->
                                    when( randomSelectedPokemon )  {
                                        is ResultState.Success -> {
                                            deleteAllPokemonData()
                                            insertPokemonIntoDatabase(randomSelectedPokemon.data as MainPokemon)
                                            _pokemonMutableLiveData.value = randomSelectedPokemon
                                        }
                                        is ResultState.Error -> {
                                            _pokemonMutableLiveData.value = randomSelectedPokemon
                                        }
                                    }
                                }.collect()

                        }
                        is ResultState.Error -> {
                            _pokemonMutableLiveData.value = allPokemons
                        }
                        else -> {
                            val errorDefault = ResultState.Error("", null)
                            _pokemonMutableLiveData.value = errorDefault
                        }
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private suspend fun deleteAllPokemonData() {
        coroutineScope {
            val deferreds = listOf(
                async { dbPokemon.pokemonDAO().clearMainPokemonData() },
                async { dbPokemon.pokemonDAO().clearPokemonStatsData() },
                async { dbPokemon.pokemonDAO().clearMPokemonMovesData() }
            )
            deferreds.awaitAll()
        }
    }

    private suspend fun insertPokemonIntoDatabase(pokemonData: MainPokemon) {

        val pokemonMain =
            dbMapper?.mapDomainMainPokemonToDBMainPokemon(pokemonData) ?: DBMainPokemon()
        dbPokemon.pokemonDAO().insertMainPokemonData(pokemonMain)

        val pokemonStats =
            dbMapper?.mapDomainPokemonStatsToDbPokemonStats(pokemonData.stats) ?: listOf()
        dbPokemon.pokemonDAO().insertStatsPokemonData(pokemonStats)

        val pokemonMoves =
            dbMapper?.mapDomainPokemonMovesToDbPokemonMoves(pokemonData.moves) ?: listOf()
        dbPokemon.pokemonDAO().insertMovesPokemonData(pokemonMoves)
    }

    private suspend fun getAllPokemonData(): ResultState<*> {
        return pokemonRepository.getAllPokemons(100, 0)
    }

}

