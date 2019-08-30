package com.WingedVampires.parkingstar.commons.experimental.preference

import com.example.studentsmanager.commons.experimental.CommonContext
import com.orhanobut.hawk.Hawk

object CommonPreferences {
    var isLogin by hawk("is_login", false)

    var usetId by hawk("user_id", "")

    fun clear() {
        Hawk.deleteAll()
        CommonContext.defaultSharedPreferences.edit().clear().apply()
    }
}