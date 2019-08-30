package com.WingedVampires.parkingstar.view.activities

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import es.dmoral.toasty.Toasty

class RegisterActivity : AppCompatActivity() {

    lateinit var mLoading: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_register)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.ThemeColor)

        val registerButton = findViewById<TextView>(R.id.tv_register_button)
        val usernameInput = findViewById<EditText>(R.id.et_register_account_input)
        val passwordInput = findViewById<EditText>(R.id.et_register_password_input)
        val npasswordInput = findViewById<EditText>(R.id.et_register_npassword_input)
        mLoading = findViewById(R.id.cl_register_loading)


        registerButton.setOnClickListener {
            usernameInput.isFocusable = false
            usernameInput.isFocusableInTouchMode = true
            passwordInput.isFocusable = false
            passwordInput.isFocusableInTouchMode = true
            npasswordInput.isFocusable = false
            npasswordInput.isFocusableInTouchMode = true
            hideSoftInputMethod()

            if (usernameInput.text.isNotBlank() && passwordInput.text.isNotBlank() && npasswordInput.text.isNotBlank()) {
                if (passwordInput.text == npasswordInput.text) {
                    if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
                    mLoading.visibility = View.GONE
                    Toasty.success(this, "注册成功", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toasty.warning(
                        this,
                        "密码填写有误",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toasty.warning(
                    this,
                    "请补全您的用户名和密码",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }

    private fun hideSoftInputMethod() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.apply {
            hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }
}
