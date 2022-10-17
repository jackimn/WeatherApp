package com.example.walkingpark.data.repository

import android.location.Address
import android.util.Log
import com.example.walkingpark.constants.ADDRESS
import com.example.walkingpark.constants.Common
import com.example.walkingpark.data.source.ApiDataSource
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {

    fun startStationApi(addresses: List<String>) = apiDataSource.getStationApi(getQuery(addresses))

    private fun getQuery(addresses: List<String>): Map<String, String> {

        val addressMap = HashMap<Char, String>()
        addresses.stream().forEach {
            for (enum in ADDRESS.values()) {
                if (it[it.lastIndex] == enum.text && addressMap[enum.text] == null) {
                    addressMap[enum.text] = it
                }
            }
        }
        Log.e("StationApiRepository.java", addresses.toString())
        return getQuery(addressMap)
    }

    private fun getQuery(addressMap: HashMap<Char, String>) = mapOf(
        "returnType" to "json",
        "addr" to handleAddressNotFound(addressMap)
    )

    // 올바른 주소를 찾을 수 없는 에러 핸들링
    private fun handleAddressNotFound(addressMap: HashMap<Char, String>)=
        addressMap.run {
            try {
                addressMap[ADDRESS.SI.text]!!.split("시")[0]
            }
            // 해외이거나 현재 지역에서 주소형식을 찾을 수 없는경우.
            catch (e: NullPointerException) {
                Common.NO_ADDRESS
            }
        }

}