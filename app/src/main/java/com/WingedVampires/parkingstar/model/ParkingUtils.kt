package com.WingedVampires.parkingstar.model

import com.amap.api.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object ParkingUtils {

    fun GCJ2BD(bd: LatLng): LatLng {
        val x = bd.longitude
        val y = bd.latitude
        val z = sqrt(x * x + y * y) + 0.00002 * sin(y * Math.PI)
        val theta = atan2(y, x) + 0.000003 * cos(x * Math.PI)
        val tempLon = z * cos(theta) + 0.0065
        val tempLat = z * sin(theta) + 0.006
        return LatLng(tempLat, tempLon)
    }

    fun BD2GCJ(bd: LatLng): LatLng {
        val x = bd.longitude - 0.0065
        val y = bd.latitude - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI)
        val theta = atan2(y, x) - 0.000003 * Math.cos(x * Math.PI)

        val lng = z * cos(theta)//lng
        val lat = z * sin(theta)//lat
        return LatLng(lat, lng)
    }

}