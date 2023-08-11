package com.example.mymovieapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

class NetworkUtils constructor(@ApplicationContext private val context: Context) {

    suspend fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                val result = CoroutineScope(Dispatchers.IO).async {
                    DoesNetworkHaveInternet.execute(connectivityManager.activeNetwork?.socketFactory)
                }
                return result.await()
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    object DoesNetworkHaveInternet {

        fun execute(socketFactory: SocketFactory?): Boolean {
            // Make sure to execute this on a background thread.
            return try {
                socketFactory?.let {
                    val socket =
                        socketFactory.createSocket() ?: throw IOException("Socket is null.")
                    socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                    socket.close()
                    true
                } ?: false
            } catch (e: IOException) {
                false
            }
        }
    }
}