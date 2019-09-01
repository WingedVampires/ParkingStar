package com.WingedVampires.parkingstar.model

import com.WingedVampires.parkingstar.commons.CommonBody
import com.WingedVampires.parkingstar.commons.ServiceFactory
import com.WingedVampires.parkingstar.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkingService {

    @GET("users/register")
    fun register(@Query("user_name") user_name: String, @Query("password") password: String): Deferred<CommonBody<String>>

    @GET("users/login")
    fun login(@Query("user_name") userName: String, @Query("password") password: String): Deferred<CommonBody<List<UserInfo>>>

    @GET("users/logout")
    fun logout(
        @Query("user_name") userName: String = CommonPreferences.userName,
        @Query("password") password: String = CommonPreferences.password
    ): Deferred<CommonBody<String>>


    @GET("users/listAllInfoOfUser")
    fun listAllInfoOfUser(@Query("user_name") userName: String = CommonPreferences.userName): Deferred<CommonBody<List<UserInfo>>>

    @GET("users/addInfo")
    fun addInfo(
        @Query("gender") gender: String?,
        @Query("phone") phone: String?,
        @Query("chinaID") chinaID: String?,
        @Query("real_name") real_name: String?,
        @Query("user_name") user_name: String = CommonPreferences.userName
    ): Deferred<CommonBody<String>>

    @GET("parking/listAllInfoOfParking")
    fun getParkingInfo(@Query("parking_id") parkingId: String): Deferred<CommonBody<List<Parking>>>

    @GET("parking/listAllParking")
    fun getParkings(): Deferred<CommonBody<List<Parking>>>

    @GET("parking/Reserve")
    fun reserve(
        @Query("parking_id") parkingId: String,
        @Query("reserved") reserved: Int,
        @Query("user_id") userId: String = CommonPreferences.userId
    ): Deferred<CommonBody<String>>

    @GET("parking/cancelReserve")
    fun cancelReserve(
        @Query("parking_id") parkingId: String,
        @Query("reserved") reserved: Int,
        @Query("user_id") userId: String = CommonPreferences.userId
    ): Deferred<CommonBody<String>>

    @GET("parking/listAllReserve")
    fun listAllReserve(@Query("user_id") userId: String = CommonPreferences.userId): Deferred<CommonBody<List<Reservation>>>


    @GET("money/applyForMonth")
    fun applyForMonth(
        @Query("parking_id") parkingId: String,
        @Query("car_id") reserved: String,
        @Query("user_id") userId: String = CommonPreferences.userId,
        @Query("user_name") userName: String = CommonPreferences.userName
    ): Deferred<CommonBody<String>>

    @GET("money/cancelForMonth")
    fun cancelForMonth(
        @Query("parking_id") parkingId: String,
        @Query("car_id") reserved: String,
        @Query("user_id") userId: String = CommonPreferences.userId,
        @Query("user_name") userName: String = CommonPreferences.userName
    ): Deferred<CommonBody<String>>

    @GET("money/listUserAllMonth")
    fun listUserAllMonth(@Query("user_id") userId: String = CommonPreferences.userId): Deferred<CommonBody<List<Monthly>>>

    @GET("money/listAllFee")
    fun listAllFee(): Deferred<CommonBody<List<Fee>>>

    @GET("/users/listAllCarOfUser")
    fun listAllCarOfUser(@Query("user_id") userId: String = CommonPreferences.userId): Deferred<CommonBody<List<Car>>>

    @GET("/users/addCar")
    fun addCar(
        @Query("car_id") reserved: String,
        @Query("user_id") userId: String = CommonPreferences.userId
    ): Deferred<CommonBody<String>>

    @GET("/users/deleteCar")
    fun deleteCar(
        @Query("car_id") reserved: String,
        @Query("user_id") userId: String = CommonPreferences.userId
    ): Deferred<CommonBody<String>>

    companion object : ParkingService by ServiceFactory()
}

data class UserInfo(
    val chinaId: String?,
    val gender: String?,
    val password: String,
    val phone: String?,
    val rank: Int,
    val real_name: String?,
    val user_id: String,
    val user_name: String
)

data class Parking(
    val hour_fee: Int,
    val is_preserve: Boolean,
    val money_rule: String,
    val month_fee: Int,
    val parking_id: String,
    val parking_name: String,
    val parking_position: String,
    val preserved: String,
    val total_parkingSpace: Int,
    val user_id: String
)

data class Reservation(
    val parking_id: String,
    val reserve_id: String,
    val reserved: Int,
    val reserved_status: String,
    val reserved_time: String,
    val user_id: String
)

data class Monthly(
    val car_id: String,
    val current_status: String,
    val end_time: String,
    val month_pay_id: Int,
    val parking_id: String,
    val start_time: String,
    val user_id: String,
    val user_name: String
)

data class Fee(
    val fee_id: String,
    val fee_type: String,
    val price: Double
)

data class Car(
    val car_id: String,
    val car_type: String?,
    val user_id: String
)