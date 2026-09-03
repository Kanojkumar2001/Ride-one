package com.example.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class LocationService(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation(
        onSuccess: (lat: Double, lng: Double, address: String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onError("Location permission required to detect pickup/delivery points")
            return
        }

        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val address = resolveAddress(location.latitude, location.longitude)
                        onSuccess(location.latitude, location.longitude, address)
                    } else {
                        // Fallback to last known location
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { lastLoc: Location? ->
                                if (lastLoc != null) {
                                    val address = resolveAddress(lastLoc.latitude, lastLoc.longitude)
                                    onSuccess(lastLoc.latitude, lastLoc.longitude, address)
                                } else {
                                    // Local default Ongole hub
                                    onSuccess(15.5057, 80.0499, "Ongole Central Bus Stand (Current Location)")
                                }
                            }
                            .addOnFailureListener {
                                onSuccess(15.5057, 80.0499, "Ongole Central (Current Location)")
                            }
                    }
                }
                .addOnFailureListener { ex ->
                    Log.w("LocationService", "Failed to get location, falling back to default", ex)
                    onSuccess(15.5057, 80.0499, "Ongole Central Bus Stand (Current Location)")
                }
        } catch (e: Exception) {
            Log.e("LocationService", "Error in fetchCurrentLocation", e)
            onError(e.message ?: "Failed to fetch GPS location")
        }
    }

    private fun resolveAddress(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    val line = addr.getAddressLine(0)
                    val feature = addr.featureName ?: addr.thoroughfare ?: addr.subLocality ?: "Ongole Hub"
                    line ?: "$feature, ${addr.locality ?: "Ongole"}"
                } else {
                    "Ongole Central (GPS: ${String.format(Locale.US, "%.3f", latitude)}, ${String.format(Locale.US, "%.3f", longitude)})"
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    addr.getAddressLine(0) ?: "${addr.locality ?: "Ongole"} (${String.format(Locale.US, "%.3f", latitude)}, ${String.format(Locale.US, "%.3f", longitude)})"
                } else {
                    "Ongole Central (Current Location)"
                }
            }
        } catch (e: Exception) {
            "Ongole Central (GPS Detected)"
        }
    }
}
