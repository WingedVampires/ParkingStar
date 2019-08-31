package com.WingedVampires.parkingstar.model

import android.content.Context
import com.WingedVampires.parkingstar.commons.experimental.preference.hawk
import com.amap.api.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object ParkingUtils {

    const val PAKING_INDEX = "parkingIndex"

    var cars by hawk("USER_CARS", arrayListOf<Car>())

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

    fun isAvilible(context: Context, packageName: String): Boolean {
        val packageManager = context.getPackageManager()//获取packagemanager
        val pinfo = packageManager.getInstalledPackages(0)//获取所有已安装程序的包信息
        val pName = ArrayList<String>()//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (i in pinfo!!.indices) {
                val pn = pinfo!!.get(i).packageName
                pName.add(pn)
            }
        }
        return pName.contains(packageName)//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

}