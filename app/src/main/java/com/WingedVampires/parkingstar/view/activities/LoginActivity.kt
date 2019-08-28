package com.WingedVampires.parkingstar.view.activities


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.WingedVampires.parkingstar.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
    }
}
