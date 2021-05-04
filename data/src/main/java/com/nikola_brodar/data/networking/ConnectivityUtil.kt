package com.nikola_brodar.data.networking

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ConnectivityUtil @Inject constructor( @ApplicationContext val context: Context) {

    fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetwork != null
    }
}