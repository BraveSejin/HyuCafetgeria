package com.sejin.hyucafeteria

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import com.sejin.hyucafeteria.utilities.toast
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    private val cm by lazy { getSystemService(ConnectivityManager::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkInternetStateAndFinish()
        registerNetworkCallback()
    }

    fun checkInternetStateAndFinish() {
        val currentNetwork = cm.activeNetwork
        if (currentNetwork == null) {
            toast("인터넷 연결 상태를 확인해주세요")
            Thread.sleep(2000)
            finish()
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d("Test", "wifi available")
        }

        override fun onLost(network: Network) {
            Handler(Looper.getMainLooper()).postDelayed(
                { checkInternetStateAndFinish() }, 3000
            )
        }
    }

    private fun registerNetworkCallback() {
        val cm = getSystemService(ConnectivityManager::class.java)
        val wifiNetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
        cm.registerNetworkCallback(wifiNetworkRequest, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        val cm = getSystemService(ConnectivityManager::class.java)
        cm.unregisterNetworkCallback(networkCallback)
    }
}