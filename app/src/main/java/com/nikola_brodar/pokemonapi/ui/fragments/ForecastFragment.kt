package com.nikola_brodar.pokemonapi.ui.fragments
//
//import android.content.ContentValues
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Observer
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.nikola_brodar.domain.ResultState
//import com.nikola_brodar.domain.model.Forecast
//import com.nikola_brodar.pokemonapi.databinding.FragmentForecastBinding
//import com.nikola_brodar.pokemonapi.ui.adapters.ForecastAdapter
//import com.nikola_brodar.pokemonapi.viewmodels.ForecastViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_pokemon.*
//import kotlinx.android.synthetic.main.fragment_forecast.*
//
//@AndroidEntryPoint
//class ForecastFragment : Fragment() {
//
//    val forecastViewModel: ForecastViewModel by viewModels()
//
//    private lateinit var forecastAdapter: ForecastAdapter
//    var weatherLayoutManager: LinearLayoutManager? = null
//
//    lateinit var binding: FragmentForecastBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?): View? {
//
//        binding = FragmentForecastBinding.inflate(inflater, container, false)
//        context ?: return binding.root
//
//        activity?.tvToolbarTitle?.text = "FORECAST"
//
//        return binding.root
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        initializeUi()
//
//        forecastViewModel.forecastList.observe(this@ForecastFragment, Observer { items ->
//            when ( items ) {
//                is ResultState.Success -> {
//                    successUpdateUi(items)
//                }
//                is ResultState.Error -> {
//                    printOutExceptionInsideLog(items)
//                }
//            }
//        })
//
//        forecastViewModel.getForecastFromNetwork("London")
//    }
//
//    private fun successUpdateUi(items: ResultState.Success<*>) {
//        val forecastList = items.data as Forecast
//        Log.d(ContentValues.TAG, "Data is: ${forecastList.forecastList.joinToString { "-" }}")
//        progressBar.visibility = View.GONE
//        forecastAdapter.updateDevices(forecastList.forecastList.toMutableList())
//    }
//
//    private fun printOutExceptionInsideLog(items: ResultState.Error) {
//        val exceptionForecast = items.exception
//        Log.d(ContentValues.TAG, "Exception inside forecast fragment is: ${   exceptionForecast}")
//    }
//
//    private fun initializeUi() {
//
//        tvForecast.text = "City name: London. Forecast for next 5 days: "
//
//        weatherLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//
//        forecastAdapter = ForecastAdapter( mutableListOf() )
//
//        binding.forecastList.apply {
//            layoutManager = weatherLayoutManager
//            adapter = forecastAdapter
//        }
//        binding.forecastList.adapter = forecastAdapter
//
//        binding.btnRoomOldWeatherData.setOnClickListener {
//            val direction =
//                ForecastFragmentDirections.forecastFragmentToForecastDatabaseFragment( cityName = "London" )
//            findNavController().navigate(direction)
//        }
//    }
//
//}