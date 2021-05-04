package com.nikola_brodar.pokemonapi.ui.activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikola_brodar.domain.ResultState
import com.nikola_brodar.domain.model.Forecast
import com.nikola_brodar.pokemonapi.R
import com.nikola_brodar.pokemonapi.databinding.ActivityWeatherBinding
import com.nikola_brodar.pokemonapi.ui.adapters.ForecastAdapter
import com.nikola_brodar.pokemonapi.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*


class WeatherActivity : BaseActivity(R.id.no_internet_layout) {

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var forecastAdapter: ForecastAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_weather)



//        binding = FragmentForecastBinding.inflate(inflater, container, false)
//        context ?: return binding.root
//
//        return binding.root


        setSupportActionBar(findViewById(R.id.toolbarWeather))
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        initializeUi()

        forecastViewModel.forecastList.observe(this, Observer { items ->
            when ( items ) {
                is ResultState.Success -> {
                    successUpdateUi(items)
                }
                is ResultState.Error -> {
                    printOutExceptionInsideLog(items)
                }
            }
        })

        forecastViewModel.getForecastFromNetwork("London")
    }

    private fun successUpdateUi(items: ResultState.Success<*>) {
        val forecastList = items.data as Forecast
        Log.d(ContentValues.TAG, "Data is: ${forecastList.forecastList.joinToString { "-" }}")
        //progressBar.visibility = View.GONE
        forecastAdapter.updateDevices(forecastList.forecastList.toMutableList())
    }

    private fun printOutExceptionInsideLog(items: ResultState.Error) {
        val exceptionForecast = items.exception
        Log.d(ContentValues.TAG, "Exception inside forecast fragment is: ${   exceptionForecast}")
    }

    private fun initializeUi() {

        //tvForecast.text = "City name: London. Forecast for next 5 days: "

        weatherLayoutManager = LinearLayoutManager(this@WeatherActivity, RecyclerView.VERTICAL, false)

        forecastAdapter = ForecastAdapter( mutableListOf() )

        //binding.forecastList.apply {
            //layoutManager = weatherLayoutManager
            //adapter = forecastAdapter
        //}
        //binding.forecastList.adapter = forecastAdapter

        //binding.btnRoomOldWeatherData.setOnClickListener {
//            val direction =
//                ForecastFragmentDirections.forecastFragmentToForecastDatabaseFragment( cityName = "London" )
//            findNavController().navigate(direction)
        //}
    }


    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

}