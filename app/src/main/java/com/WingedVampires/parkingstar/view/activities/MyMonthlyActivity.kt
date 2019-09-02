package com.WingedVampires.parkingstar.view.activities

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.WingedVampires.parkingstar.commons.experimental.extensions.awaitAndHandle
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.withItems
import com.WingedVampires.parkingstar.model.LiveParkingManager
import com.WingedVampires.parkingstar.model.Monthly
import com.WingedVampires.parkingstar.model.ParkingService
import com.WingedVampires.parkingstar.model.ParkingUtils
import com.WingedVampires.parkingstar.view.items.MyTitleItem
import com.WingedVampires.parkingstar.view.items.MyreservationItem
import com.WingedVampires.parkingstar.view.items.myTitleItem
import com.WingedVampires.parkingstar.view.items.myreservationItem
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.imageResource

class MyMonthlyActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }
    private lateinit var mLoading: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_my_monthly)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.ThemeColor)

        val mToolBar = findViewById<Toolbar>(R.id.tb_mymonthly)
        val refresh = findViewById<ImageView>(R.id.iv_mymonthly_refresh)
        mLoading = findViewById(R.id.cl_mymonthly_loading)
        recyclerView = findViewById(R.id.rv_mymonthly)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mToolBar.apply {
            title = "MY MONTHLY"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        refreshMyMonthly()
        refresh.setOnClickListener { refreshMyMonthly() }
    }

    private fun refreshMyMonthly() {
        LiveParkingManager.refreshParkingInfo()

        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
            itemManager.refreshAll { }
            val totalList = ParkingService.listUserAllMonth().awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@MyMonthlyActivity, "加载失败", Toast.LENGTH_SHORT).show()
            }?.data ?: return@launch

            val listOfRunning = mutableListOf<Item>().apply {
                totalList.filter { it.current_status == "使用中" }.forEach { r ->
                    myreservationItem(
                        r.parking_id,
                        ParkingUtils.parkings[r.parking_id],
                        r.car_id,
                        "${r.start_time} - ${r.end_time}",
                        true
                    ) { item ->
                        deleteItem(r, item)
                    }
                }
            }
            val listOfChecking = mutableListOf<Item>().apply {
                totalList.filter { it.current_status == "待审核" }.forEach { r ->
                    myreservationItem(
                        r.parking_id,
                        ParkingUtils.parkings[r.parking_id],
                        r.car_id,
                        "${r.start_time} - ${r.end_time}",
                        true
                    ) { item ->
                        deleteItem(r, item)
                    }
                }
            }
            val listOfRefuse = mutableListOf<Item>().apply {
                totalList.filter { it.current_status == "被拒绝" }.forEach { r ->
                    myreservationItem(
                        r.parking_id,
                        ParkingUtils.parkings[r.parking_id],
                        r.car_id,
                        "${r.start_time} - ${r.end_time}",
                        false
                    )
                }
            }
            val listOfCancel = mutableListOf<Item>().apply {
                totalList.filter { it.current_status == "已取消" }.forEach { r ->
                    myreservationItem(
                        r.parking_id,
                        ParkingUtils.parkings[r.parking_id],
                        r.car_id,
                        "${r.start_time} - ${r.end_time}",
                        false
                    )
                }
            }

            mLoading.visibility = View.GONE
            Toasty.success(this@MyMonthlyActivity, "加载完成", Toast.LENGTH_SHORT).show()
            itemManager.refreshAll {
                myTitleItem("使用中") { viewHolder, item ->
                    showAndCloseMore(viewHolder, item, listOfRunning)
                }
                myTitleItem("待审核") { viewHolder, item ->
                    showAndCloseMore(viewHolder, item, listOfChecking)
                }
                myTitleItem("被拒绝") { viewHolder, item ->
                    showAndCloseMore(viewHolder, item, listOfRefuse)
                }
                myTitleItem("已取消") { viewHolder, item ->
                    showAndCloseMore(viewHolder, item, listOfCancel)
                }

            }
        }
    }

    private fun deleteItem(r: Monthly, item: MyreservationItem) {
        if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val result = ParkingService.cancelForMonth(
                r.parking_id,
                r.car_id
            ).awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@MyMonthlyActivity, "删除失败", Toast.LENGTH_SHORT)
                    .show()
            } ?: return@launch
            Toasty.success(
                this@MyMonthlyActivity,
                result.message,
                Toast.LENGTH_SHORT
            ).show()

            if (result.error_code == -1) {
                itemManager.remove(item)
            }
        }
        mLoading.visibility = View.GONE


    }

    private fun showAndCloseMore(
        viewHolder: MyTitleItem.ViewHolder,
        item: MyTitleItem,
        list: MutableList<Item>
    ) {
        if (item.isOpen) {
            viewHolder.more.imageResource = R.drawable.cd_show
            itemManager.removeAll(list)
            item.isOpen = false

        } else {
            viewHolder.more.imageResource = R.drawable.cd_close
            itemManager.autoRefresh {
                addAll(itemManager.indexOf(item) + 1, list)
            }
            item.isOpen = true
        }
    }
}
