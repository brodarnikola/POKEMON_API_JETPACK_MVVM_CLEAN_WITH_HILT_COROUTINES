package com.nikola_brodar.pokemonapi.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.MainPokemon
import com.nikola_brodar.pokemonapi.R
import com.nikola_brodar.pokemonapi.databinding.ActivityPokemonBinding
import com.nikola_brodar.pokemonapi.ui.adapters.PokemonAdapter
import com.nikola_brodar.pokemonapi.viewmodels.PokemonViewModel
import kotlinx.android.synthetic.main.activity_pokemon.*


class PokemonActivity : BaseActivity(R.id.no_internet_layout) {

    var displayCurrentPokemonData = false

    val pokemonViewModel: PokemonViewModel by viewModels()

    private lateinit var pokemonAdapter: PokemonAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(findViewById(R.id.toolbarWeather))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        displayCurrentPokemonData = intent.getBooleanExtra("displayCurrentPokemonData", false)
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        initializeUi()

        pokemonViewModel.mainPokemonData.observe(this, Observer { items ->
            successUpdateUi(items)
        })

        if( displayCurrentPokemonData )
            pokemonViewModel.getAllPokemonDataFromLocalStorage()
        else
            pokemonViewModel.getPokemonData()

        floatingButton.setOnClickListener {
            Log.d(ContentValues.TAG, "AAAAA")
        }
    }

    private fun successUpdateUi(pokemonData: MainPokemon) {
        Log.d(ContentValues.TAG, "Data is: ${pokemonData.stats.joinToString { "-" + it.stat.name }}")
        progressBar.visibility = View.GONE
        tvName.text = "Name: " + pokemonData.name

        Glide.with(this)
            .load( pokemonData.sprites.backDefault)
            .placeholder(R.drawable.garden_tab_selector)
            .error(R.drawable.garden_tab_selector)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivBack)

        Glide.with(this)
            .load( pokemonData.sprites.frontDefault)
            .placeholder(R.drawable.garden_tab_selector)
            .error(R.drawable.garden_tab_selector)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivFront)
        pokemonAdapter.updateDevices(pokemonData.stats.toMutableList())
    }

    private fun initializeUi() {

        weatherLayoutManager = LinearLayoutManager(this@PokemonActivity, RecyclerView.VERTICAL, false)

        pokemonAdapter = PokemonAdapter( mutableListOf() )

        binding.pokemonList.apply {
            layoutManager = weatherLayoutManager
            adapter = pokemonAdapter
        }
        binding.pokemonList.adapter = pokemonAdapter

        binding.btnRoomOldWeatherData.setOnClickListener {
            val intent = Intent(this, PokemonMovesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

}