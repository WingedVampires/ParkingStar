package com.WingedVampires.parkingstar.model

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.amap.api.maps.MapView

class MyMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }

        return super.dispatchTouchEvent(event)
    }
}