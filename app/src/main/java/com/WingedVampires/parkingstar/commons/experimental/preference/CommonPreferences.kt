package com.WingedVampires.parkingstar.commons.experimental.preference

import com.example.studentsmanager.commons.experimental.CommonContext
import com.orhanobut.hawk.Hawk

object CommonPreferences {
    var isLogin by hawk("is_login", false)

    var userId by hawk("user_id", "")

    var userName by hawk("user_name", "")

    var password by hawk("user_password", "")

    var rank by hawk("rank", 0)

    fun clear() {
        Hawk.deleteAll()
        CommonContext.defaultSharedPreferences.edit().clear().apply()
    }
}