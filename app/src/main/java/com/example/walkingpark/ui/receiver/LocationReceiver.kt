package com.example.walkingpark.ui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.walkingpark.constants.Common
import com.example.walkingpark.ui.service.LocationService
import dagger.hilt.android.AndroidEntryPoint



// 현재 사용하지 않음.
// 액티비티(프래그먼트) 와 서비스 사이 통신을 중개, 서비스를 제어
@AndroidEntryPoint
class LocationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, result: Intent) {
        result.action?.let {
            startLocationRequest(context, it)
        }
    }

    // GPS 퍼미션이 허용되어, GPS 획득 요청 수행.
    private fun startLocationRequest(context: Context, intentFilter: String) {
        Log.e("LocationReceiver", intentFilter)
        Intent(context, LocationService::class.java)
            .apply {
                putExtra("intent-filter", intentFilter)
            }.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    context.startForegroundService(this)
                else
                    context.startService(this)
            }
    }


/*    @SuppressLint("MissingPermission")
    private fun setLocationInit(context: Context){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("ParkMapsService::class", "퍼미션 허용 안됨")
            return
        } else {
            val src = CancellationTokenSource()
            val ct: CancellationToken = src.token
            fusedLocationProviderClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                ct
            ).addOnFailureListener {
                Log.e("fusedLocationProvider", "fail")
            }.addOnSuccessListener {
                Log.e("fusedLocationProvider", "${it.latitude} ${it.longitude}")

                // parsingAddressMap(context, it.latitude, it.longitude)

            }
        }
    }

    // 주기적인 위치 업데이트 수행
    @SuppressLint("MissingPermission")
    private fun setLocationUpdate(context: Context
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("ParkMapsService", "퍼미션 허용 안됨")
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            ).addOnCompleteListener {
                Log.e("LocationServiceRepository : ", "LocationUpdateCallbackRegistered.")
            }
        }
    }*/
}