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
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.withItems
import com.WingedVampires.parkingstar.view.items.myTitleItem
import com.WingedVampires.parkingstar.view.items.myreservationItem
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.imageResource

class MyReservationActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }
    private lateinit var mLoading: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_myreservation)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.ThemeColor)

        val mToolBar = findViewById<Toolbar>(R.id.tb_myreservation)
        val refresh = findViewById<ImageView>(R.id.iv_myreservation_refresh)
        mLoading = findViewById(R.id.cl_reservation_loading)
        recyclerView = findViewById(R.id.rv_myreservation)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mToolBar.apply {
            title = "MY RESERVATION"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        refreshMyReservation()
        refresh.setOnClickListener { refreshMyReservation() }
    }

    private fun refreshMyReservation() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
            val list = mutableListOf<Item>().apply {
                myreservationItem("jeij", "fjiej", "jfi", "jfi")
                myreservationItem("jeij", "fjiej", "jfi", "jfi")
                myreservationItem("jeij", "fjiej", "jfi", "jfi")
            }
            mLoading.visibility = View.GONE
            Toasty.success(this@MyReservationActivity, "加载完成", Toast.LENGTH_SHORT).show()
            itemManager.refreshAll {
                myTitleItem("进行中") { viewHolder, item ->
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
        }

    }
}


