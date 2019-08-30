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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.experimental.preference.CommonPreferences
import com.example.studentsmanager.commons.experimental.CommonContext
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    lateinit var mLoading: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.ThemeColor)

        CommonContext.registerApplication(this@LoginActivity.application)

        val loginButton = findViewById<ImageView>(R.id.iv_login_button)
        val registerText = findViewById<TextView>(R.id.tv_login_register)
        val usernameInput = findViewById<EditText>(R.id.et_login_account_input)
        val passwordInput = findViewById<EditText>(R.id.et_login_password_input)
        mLoading = findViewById(R.id.cl_login_loading)

        if (CommonPreferences.isLogin) {
            startActivity<MainActivity>()
            finish()
        }

        loginButton.setOnClickListener {
            usernameInput.isFocusable = false
            usernameInput.isFocusableInTouchMode = true
            passwordInput.isFocusable = false
            passwordInput.isFocusableInTouchMode = true
            hideSoftInputMethod()

            if (usernameInput.text.isNotBlank() && passwordInput.text.isNotBlank()) {
                if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE

                mLoading.visibility = View.GONE
                Toasty.success(this, "登录成功", Toast.LENGTH_SHORT).show()
                it.context.startActivity<MainActivity>()
                CommonPreferences.isLogin = true
                finish()
            } else {
                Toasty.warning(
                    this,
                    "请补全您的用户名和密码",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        registerText.setOnClickListener {
            usernameInput.isFocusable = false
            usernameInput.isFocusableInTouchMode = true
            passwordInput.isFocusable = false
            passwordInput.isFocusableInTouchMode = true

            it.context.startActivity<RegisterActivity>()
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
