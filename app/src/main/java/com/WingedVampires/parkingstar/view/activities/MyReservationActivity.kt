package com.WingedVampires.parkingstar.view.activities

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.ImageView
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.ui.rec.withItems

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
        mToolBar.apply {
            title = "MYRESERVATION"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        refresh.setOnClickListener { refreshMyReservation() }
    }

    private fun refreshMyReservation() {

    }
}
