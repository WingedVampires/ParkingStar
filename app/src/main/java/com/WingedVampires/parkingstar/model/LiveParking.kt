package com.WingedVampires.parkingstar.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.WingedVampires.parkingstar.commons.experimental.cache.Cache
import com.WingedVampires.parkingstar.commons.experimental.cache.RefreshState
import com.WingedVampires.parkingstar.commons.experimental.cache.hawk
import com.WingedVampires.parkingstar.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class ParkingInfo(
    val parkings: List<Parking>,
    val cars: List<Car>,
    val fees: List<Fee>,
    val cache: Boolean
)

val parkingCacheKey = "PARKING_FULL_INFO_CACHE"
val parkingInfoCache = Cache.hawk<ParkingInfo>(parkingCacheKey)

object LiveParkingManager {

    private val parkingInfoLiveData = object : MutableLiveData<RefreshState<ParkingInfo>>() {
        override fun onActive() {
            super.onActive()
            refreshParkingInfo()
        }
    }

    fun getParkingLiveData(): LiveData<RefreshState<ParkingInfo>> = parkingInfoLiveData

    init {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val cache = parkingInfoCache.get().await()
            cache?.let { parkingInfoLiveData.postValue(RefreshState.Success(it.copy(cache = true))) } // 第一次Load Cache
        }
    }

    fun refreshParkingInfo(forceReload: Boolean = true) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            parkingInfoLiveData.postValue(RefreshState.Failure(throwable))
        }

        GlobalScope.launch(Dispatchers.Main + coroutineExceptionHandler) {

            parkingInfoLiveData.postValue(RefreshState.Refreshing())

            if (!forceReload) {
                parkingInfoCache.get().await()
                    ?.let { parkingInfoLiveData.postValue(RefreshState.Success(it)) } // Load Cache
            }


            val parkings = ParkingService.getParkings().await().data
                ?: throw IllegalStateException("停车场数据为空 联系开发者解决")
            val cars = ParkingService.listAllCarOfUser().await().data
                ?: throw IllegalStateException("汽车数据为空 联系开发者解决")
            val fees = ParkingService.listAllFee().await().data
                ?: throw IllegalStateException("费用数据为空 联系开发者解决")

            ParkingUtils.cars = arrayListOf<Car>().apply { addAll(cars) }
            parkings.forEach {
                ParkingUtils.parkings[it.parking_id] = it.parking_name
            }

            val parkingInfo = ParkingInfo(parkings, cars, fees, cache = false)
            parkingInfoCache.set(parkingInfo)
            parkingInfoLiveData.postValue(RefreshState.Success(parkingInfo))
        }
    }
}