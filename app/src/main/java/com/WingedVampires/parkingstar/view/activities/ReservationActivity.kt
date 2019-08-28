package com.WingedVampires.parkingstar.view.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Window
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode

class ReservationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_reservation)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this@ReservationActivity, R.color.ThemeColor)
        val mToolBar = findViewById<Toolbar>(R.id.tb_reservation)
        mToolBar.apply {
            title = "RESERVATION"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
    }
}
