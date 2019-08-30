package com.WingedVampires.parkingstar.view.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.ui.rec.withItems
import com.WingedVampires.parkingstar.model.ParkingUtils
import com.WingedVampires.parkingstar.view.items.parkingCarItem
import com.amap.api.maps.model.LatLng
import com.amap.api.services.geocoder.GeocodeQuery
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeResult
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundResource
import java.lang.reflect.Field


class ReservationActivity : AppCompatActivity() {
    private lateinit var mBanner: Banner
    private lateinit var mLoading: ConstraintLayout
    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_reservation)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this@ReservationActivity, R.color.ThemeColor)
        mBanner = findViewById(R.id.b_reservation)
        val mToolBar = findViewById<Toolbar>(R.id.tb_reservation)
        val refresh = findViewById<ImageView>(R.id.iv_reservation_refresh)
        val guide = findViewById<ImageView>(R.id.iv_reservation_guide)
        val reserveButton = findViewById<TextView>(R.id.tv_reservation_reserve)
        val monthlyButton = findViewById<TextView>(R.id.tv_reservation_monthly)
        val reservationList = findViewById<LinearLayout>(R.id.ll_reservation_reservation)
        val bundle = intent.extras
        val index = bundle?.getInt(ParkingUtils.PAKING_INDEX)
        val pics = mutableListOf(R.drawable.ms_no_pic, R.drawable.ms_no_pic, R.drawable.ms_no_pic)
        mLoading = findViewById(R.id.cl_reservation_loading)
        recyclerView = findViewById(R.id.rv_reservation)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        mToolBar.apply {
            title = "RESERVATION"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        mBanner.apply {
            //设置图片加载器
            setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                    Glide.with(context).load(path).error(R.drawable.ms_no_pic).into(imageView)
                }
            })
            //设置banner样式
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            //设置图片集合
            setImages(pics)
            //设置自动轮播，默认为true
            isAutoPlay(true)
            //设置指示器位置（当banner模式中有指示器时）
            setIndicatorGravity(BannerConfig.RIGHT)
            start()
        }
        refreshParking()
        guide.setOnClickListener { guide("南开大学") }
        refresh.setOnClickListener {
            refreshParking()
        }
        reserveButton.setOnClickListener {
            reservationList.visibility =
                if (reservationList.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        monthlyButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this@ReservationActivity)
                .setMessage("Do you want to park this parking lot for a month?")
                .setCancelable(false)
                .setPositiveButton("No, I think about it again.") { _, _ ->

                }
                .setNegativeButton("OK,thanks") { _, _ ->

                }
                .create()
            dialog.show()

            try {
                val mAlert: Field = AlertDialog::class.java.getDeclaredField("mAlert")
                mAlert.isAccessible = true
                val mAlertController = mAlert.get(dialog)
                val mMessage: Field =
                    mAlertController.javaClass.getDeclaredField("mMessageView")
                mMessage.isAccessible = true
                val mMessageView = mMessage.get(mAlertController) as TextView
                mMessageView.setTextColor(
                    ContextCompat.getColor(
                        this@ReservationActivity,
                        R.color.ThemeTextColor
                    )
                )
                mMessageView.textSize = 25f
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
        }
    }

    private fun guide(address: String) {
        val geocodeSearch = GeocodeSearch(this)

        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {

            override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult, i: Int) {
            }


            override fun onGeocodeSearched(geocodeResult: GeocodeResult?, i: Int) {
                if (geocodeResult != null && geocodeResult.geocodeAddressList != null && geocodeResult.geocodeAddressList.size > 0) {
                    val geocodeAddress = geocodeResult.geocodeAddressList[0]
                    val latitude = geocodeAddress.latLonPoint.latitude//纬度
                    val longititude = geocodeAddress.latLonPoint.longitude//经度
                    val adcode = geocodeAddress.adcode//区域编码

                    if (!ParkingUtils.isAvilible(
                            this@ReservationActivity,
                            "com.autonavi.minimap"
                        )
                    ) {
                        Toasty.error(this@ReservationActivity, "请先安装高德地图客户端").show()
                        return
                    }

                    val endPoint = ParkingUtils.BD2GCJ(LatLng(latitude, longititude))//坐标转换
                    val stringBuffer =
                        StringBuffer("amapuri://route/plan/?sourceApplication=").append("ParkingStar")
                            .append("&dlat=").append(endPoint.latitude)
                            .append("&dlon=").append(endPoint.longitude)
                            .append("&dname=").append(address)
                            .append("&dev=").append(0)
                            .append("&t=").append(0)
                            .append("&style=").append(2)
                    val intent = Intent(
                        "android.intent.action.VIEW",
                        Uri.parse(stringBuffer.toString())
                    )
                    intent.setPackage("com.autonavi.minimap")
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    startActivity(intent)
                } else {
                    Toasty.error(this@ReservationActivity, "地名出错", Toast.LENGTH_SHORT).show()
                }

            }
        })


        val geocodeQuery = GeocodeQuery(address, "022")

        geocodeSearch.getFromLocationNameAsyn(geocodeQuery)

    }

    private fun refreshParking() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            if (mLoading.visibility != View.VISIBLE) mLoading.visibility = View.VISIBLE

            mLoading.visibility = View.GONE
            Toasty.success(this@ReservationActivity, "加载成功", Toast.LENGTH_SHORT).show()

            itemManager.refreshAll {
                for (i in 1..21) {
                    parkingCarItem(i, mutableListOf(2, 4, 8, 10).contains(i)) { view, viewHolder ->
                        val dialog = AlertDialog.Builder(this@ReservationActivity)
                            .setMessage("Are you sure you want to reserve a parking space for ${i}?")
                            .setCancelable(false)
                            .setPositiveButton("No, I think about it again.") { _, _ ->

                            }
                            .setNegativeButton("OK,thanks") { _, _ ->
                                viewHolder.apply {
                                    add.visibility = View.GONE
                                    not.visibility = View.VISIBLE
                                    pic.backgroundResource = R.drawable.cd_car4
                                }

                            }
                            .create()
                        dialog.show()

                        try {
                            val mAlert: Field = AlertDialog::class.java.getDeclaredField("mAlert")
                            mAlert.isAccessible = true
                            val mAlertController = mAlert.get(dialog)
                            val mMessage: Field =
                                mAlertController.javaClass.getDeclaredField("mMessageView")
                            mMessage.isAccessible = true
                            val mMessageView = mMessage.get(mAlertController) as TextView
                            mMessageView.setTextColor(
                                ContextCompat.getColor(
                                    this@ReservationActivity,
                                    R.color.ThemeTextColor
                                )
                            )
                            mMessageView.textSize = 25f
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: NoSuchFieldException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mBanner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        mBanner.stopAutoPlay()
    }
}
