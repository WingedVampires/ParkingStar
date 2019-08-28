package com.WingedVampires.parkingstar.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.WingedVampires.parkingstar.R
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.LocationSource
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle


class MapFragment : Fragment(), CompoundButton.OnCheckedChangeListener, LocationSource,
    AMapLocationListener {

    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var myLocationStyle: MyLocationStyle? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var locationClient: AMapLocationClient? = null
    private var clientOption: AMapLocationClientOption? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mv_map) as MapView
        mapView?.onCreate(savedInstanceState)
        aMap = mapView?.map
        myLocationStyle = MyLocationStyle()//初始化定位蓝点样式类
        aMap?.setMyLocationStyle(myLocationStyle)
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.setLocationSource(this)
        aMap?.setMyLocationEnabled(true)
        return view
    }

    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mListener = listener
        locationClient = AMapLocationClient(activity)
        clientOption = AMapLocationClientOption()
        locationClient?.setLocationListener(this)
        clientOption?.locationMode =
            AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//高精度定位
        clientOption?.isOnceLocationLatest = false//设置单次精确定位
        clientOption?.setOnceLocation(true)
        locationClient?.setLocationOption(clientOption)
        locationClient?.startLocation()

    }

    /**
     * 停止定位
     */
    override fun deactivate() {
        mListener = null
        if (locationClient != null) {
            locationClient?.stopLocation()
            locationClient?.onDestroy()
        }
        locationClient = null
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.errorCode == 0) {
                mListener?.onLocationChanged(aMapLocation)// 显示系统小蓝点
            } else {
                val errText = "定位失败," + aMapLocation.errorCode + ": " + aMapLocation.errorInfo
                Log.e("AmapErr", errText)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            aMap?.setMapType(AMap.MAP_TYPE_SATELLITE)
        } else {
            aMap?.setMapType(AMap.MAP_TYPE_NORMAL)
        }
    }

    /**
     * 必须重写以下方法
     */
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        locationClient?.onDestroy()
    }

}