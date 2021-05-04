package com.nikola_brodar.pokemonapi.ui.activities

import android.content.ContentValues
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
import com.nikola_brodar.domain.model.Forecast
import com.nikola_brodar.domain.model.MainPokemon
import com.nikola_brodar.pokemonapi.R
import com.nikola_brodar.pokemonapi.databinding.ActivityPokemonBinding
import com.nikola_brodar.pokemonapi.ui.adapters.ForecastAdapter
import com.nikola_brodar.pokemonapi.viewmodels.ForecastViewModel
import kotlinx.android.synthetic.main.activity_pokemon.*


class PokemonActivity : BaseActivity(R.id.no_internet_layout) {

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var forecastAdapter: ForecastAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(findViewById(R.id.toolbarWeather))
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        initializeUi()

        forecastViewModel.mainPokemonData.observe(this, Observer { items ->
            when( items ) {
                is ResultState.Success -> {
                    successUpdateUi(items)
                    val test5 = items.data as MainPokemon
                    Log.d(
                        ContentValues.TAG,
                        "onNext received: " + test5.name + " backDefault: " + test5.sprites.backDefault
                    )
                }
                is ResultState.Error -> {
                    printOutExceptionInsideLog(items)
                }
            }
        })

        forecastViewModel.getForecastFromNetwork("London")

        forecastViewModel.getPokemonData(9)
    }

    private fun successUpdateUi(items: ResultState.Success<*>) {
        val pokemonData = items.data as MainPokemon
        //Log.d(ContentValues.TAG, "Data is: ${forecastList.forecastList.joinToString { "-" }}")
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
        //forecastAdapter.updateDevices(forecastList.forecastList.toMutableList())
    }

    private fun printOutExceptionInsideLog(items: ResultState.Error) {
        val exception = items.exception
        Log.d(ContentValues.TAG, "Exception inside pokemon activity is: ${   exception}")
    }

    private fun initializeUi() {

        weatherLayoutManager = LinearLayoutManager(this@PokemonActivity, RecyclerView.VERTICAL, false)

        forecastAdapter = ForecastAdapter( mutableListOf() )

        binding.forecastList.apply {
            layoutManager = weatherLayoutManager
            adapter = forecastAdapter
        }
        binding.forecastList.adapter = forecastAdapter

        binding.btnRoomOldWeatherData.setOnClickListener {
//            val direction =
//                ForecastFragmentDirections.forecastFragmentToForecastDatabaseFragment( cityName = "London" )
//            findNavController().navigate(direction)
        }
    }


    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

}