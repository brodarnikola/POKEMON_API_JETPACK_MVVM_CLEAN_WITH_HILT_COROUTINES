package com.nikola_brodar.pokemonapi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nikola_brodar.pokemonapi.R
import com.nikola_brodar.domain.model.ForecastData
import kotlinx.android.synthetic.main.forecast_list.view.*

class ForecastAdapter(
    var forecastResultList: MutableList<ForecastData>
) : RecyclerView.Adapter<ForecastAdapter.WeatherViewHolder>() {

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindItem( forecastResultList[position] )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.forecast_database_list, parent, false)
        return WeatherViewHolder(itemView)
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTemp: TextView = itemView.tvTemp
        val tvMax: TextView = itemView.tvMax
        val tvFeelsLike: TextView = itemView.tvFeelsLike
        val tvWind: TextView = itemView.tvWind
        val tvDescription: TextView = itemView.tvDescription
        val tvDate: TextView = itemView.tvDate

        fun bindItem(item: ForecastData) {

            tvTemp.text = "Temp: " + item.main.temp
            tvMax.text = "Temp max: " + item.main.tempMax
            tvFeelsLike.text = "Feels like: " + item.main.feelsLike
            tvWind.text = "Wind Speed: " + item.wind.speed
            if( item.weather.isNotEmpty() ) {
                tvDescription.visibility = View.VISIBLE
                tvDescription.text = "Description: " + item.weather[0].description
            }
            else
                tvDescription.visibility = View.GONE
            tvDate.text = "Date and time: " + item.dateAndTime
        }
    }

    override fun getItemCount(): Int {
        return forecastResultList.size
    }

    fun updateDevices(updatedDevices: MutableList<ForecastData>) {
        forecastResultList.addAll(updatedDevices)
        notifyItemRangeInserted(forecastResultList.size, updatedDevices.size)
    }

}