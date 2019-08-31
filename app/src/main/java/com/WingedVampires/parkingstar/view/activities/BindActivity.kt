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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.cache.RefreshState
import com.WingedVampires.parkingstar.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.WingedVampires.parkingstar.commons.experimental.extensions.awaitAndHandle
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.ui.rec.withItems
import com.WingedVampires.parkingstar.model.LiveParkingManager
import com.WingedVampires.parkingstar.model.ParkingService
import com.WingedVampires.parkingstar.view.items.bindItem
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BindActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }
    private lateinit var mLoading: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_bind)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this@BindActivity, R.color.ThemeColor)

        val mToolBar = findViewById<Toolbar>(R.id.tb_bind)
        val refresh = findViewById<ImageView>(R.id.iv_bind_refresh)
        val add = findViewById<ImageView>(R.id.iv_bind_add)
        val input = findViewById<EditText>(R.id.et_bind_input)
        mLoading = findViewById(R.id.cl_bind_loading)
        recyclerView = findViewById(R.id.rv_bind)


        recyclerView.layoutManager = LinearLayoutManager(this)
        mToolBar.apply {
            title = "CAR BINDING"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        refreshBound()

        LiveParkingManager.getParkingLiveData().observeForever { refreshState ->
            if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
            when (refreshState) {
                is RefreshState.Success -> refreshState.message.apply {
                    val cars = this.cars
                    Toasty.success(this@BindActivity, "加载完成", Toast.LENGTH_SHORT).show()
                    itemManager.refreshAll {
                        cars.forEach { car ->
                            bindItem(car.car_id) {
                                if (mLoading.visibility != View.VISIBLE) mLoading.visibility =
                                    View.VISIBLE
                                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                    val result =
                                        ParkingService.deleteCar(car.car_id).awaitAndHandle {
                                            it.printStackTrace()
                                            Toasty.error(
                                                this@BindActivity,
                                                "删除失败",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } ?: return@launch

                                    Toasty.success(
                                        this@BindActivity,
                                        result.message,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                                mLoading.visibility = View.GONE
                                refreshBound()
                            }
                        }
                    }
                }
            }
            mLoading.visibility = View.GONE
        }

        add.setOnClickListener {
            if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                val result = ParkingService.addCar(input.text.toString()).awaitAndHandle {
                    it.printStackTrace()
                    Toasty.error(this@BindActivity, "删除失败", Toast.LENGTH_SHORT).show()
                } ?: return@launch
                Toasty.success(this@BindActivity, result.message, Toast.LENGTH_SHORT).show()
            }
            mLoading.visibility = View.GONE
            refreshBound()
        }

        refresh.setOnClickListener { refreshBound() }


    }

    private fun refreshBound() {
        LiveParkingManager.refreshParkingInfo(true)
    }
}
