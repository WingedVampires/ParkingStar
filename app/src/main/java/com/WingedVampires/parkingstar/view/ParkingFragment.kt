package com.WingedVampires.parkingstar.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.cache.RefreshState
import com.WingedVampires.parkingstar.commons.ui.rec.withItems
import com.WingedVampires.parkingstar.model.LiveParkingManager
import com.WingedVampires.parkingstar.model.ParkingUtils
import com.WingedVampires.parkingstar.view.activities.ReservationActivity
import com.WingedVampires.parkingstar.view.items.parkingItem
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.startActivity

class ParkingFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }
    private val layoutManager = GridLayoutManager(activity, 2)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_parking, container, false)
        recyclerView = view.findViewById(R.id.rv_parking)
        swipeRefreshLayout = view.findViewById(R.id.sr_parking_refresh)
        swipeRefreshLayout.apply {
            setColorSchemeColors(
                ContextCompat.getColor(
                    this@ParkingFragment.context!!,
                    R.color.ThemeTextColor
                )
            )
            setOnRefreshListener(this@ParkingFragment::refreshParkings)
        }

        recyclerView.layoutManager = layoutManager
        refreshParkings()

        LiveParkingManager.getParkingLiveData().observeForever { refreshState ->
            when (refreshState) {
                is RefreshState.Success -> refreshState.message.apply {
                    this@ParkingFragment.context?.let {
                        Toasty.success(it, "加载完成", Toast.LENGTH_SHORT)
                            .show()
                    }
                    swipeRefreshLayout.isRefreshing = false

                    val parkings = this.parkings
                    itemManager.refreshAll {
                        parkings.forEach {
                            parkingItem(
                                it.parking_name,
                                it.parking_position,
                                it.total_parkingSpace.toString(),
                                it.is_preserve
                            ) { view, _ ->
                                view.context.startActivity<ReservationActivity>(
                                    ParkingUtils.PAKING_INDEX to it.parking_id
                                )
                            }
                        }
                    }
                }
                is RefreshState.Refreshing -> {
                    swipeRefreshLayout.isRefreshing = true
                }
                is RefreshState.Failure -> {
                    this@ParkingFragment.context?.let {
                        Toasty.success(it, "加载失败", Toast.LENGTH_SHORT)
                            .show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
        return view
    }

    private fun refreshParkings() {
        LiveParkingManager.refreshParkingInfo()
    }
}