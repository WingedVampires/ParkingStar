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
import com.WingedVampires.parkingstar.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.WingedVampires.parkingstar.commons.experimental.extensions.awaitAndHandle
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.experimental.preference.CommonPreferences
import com.WingedVampires.parkingstar.model.ParkingService
import com.example.studentsmanager.commons.experimental.CommonContext
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

            if (usernameInput.text.toString().isNotBlank() && passwordInput.text.toString().isNotBlank()) {
                if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                    val login = ParkingService.login(
                        usernameInput.text.toString(),
                        passwordInput.text.toString()
                    ).awaitAndHandle {
                        it.printStackTrace()
                        Toasty.error(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                        mLoading.visibility = View.GONE
                    }
                    Toasty.success(
                        this@LoginActivity,
                        login?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    mLoading.visibility = View.GONE

                    if (login == null || login.error_code != -1) {
                        return@launch
                    }
                    val user = login?.data?.get(0) ?: return@launch
                    CommonPreferences.apply {
                        userId = user.user_id
                        userName = user.user_name
                        password = user.password
                        rank = user.rank
                    }

                    it.context.startActivity<MainActivity>()
                    CommonPreferences.isLogin = true
                    finish()
                }


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
