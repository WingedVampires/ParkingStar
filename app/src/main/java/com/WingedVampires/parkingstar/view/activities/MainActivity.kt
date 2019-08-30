package com.WingedVampires.parkingstar.view.activities


import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.enableLightStatusBarMode
import com.WingedVampires.parkingstar.commons.experimental.preference.CommonPreferences
import com.WingedVampires.parkingstar.view.MapFragment
import com.WingedVampires.parkingstar.view.PagerAdapter
import com.WingedVampires.parkingstar.view.ParkingFragment
import com.WingedVampires.parkingstar.view.UserFragment
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var mDlNavButtom: DrawerLayout
    private lateinit var mActionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var mNavView: NavigationView
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mPagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        enableLightStatusBarMode(true)
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.ThemeColor)

        mToolbar = findViewById(R.id.tb_main)
        mNavView = findViewById(R.id.nv_buttom)
        mViewPager = findViewById(R.id.vp_main)
        mTabLayout = findViewById(R.id.tl_main)
        mDlNavButtom = findViewById(R.id.dl_nav_buttom)

        mToolbar.apply {
            setSupportActionBar(this)

        }
        mToolbar.title = "PARKING STAR"
        setMenu()

        mPagerAdapter = PagerAdapter(
            supportFragmentManager,
            this
        ).apply {
            add(MapFragment(), "LOCATION", R.drawable.cd_earth_tab)
            add(ParkingFragment(), "PARKING", R.drawable.cd_car_fill)
            add(UserFragment(), "USER", R.drawable.cd_user_tab)
        }

        mViewPager.adapter = mPagerAdapter
        mViewPager.offscreenPageLimit = 5
        mTabLayout.apply {
            setupWithViewPager(mViewPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.ThemeTextColor
                )
            )
        }
    }

    private fun setMenu() {
        mActionBarDrawerToggle = ActionBarDrawerToggle(this, mDlNavButtom, mToolbar, 0, 0);
        mDlNavButtom.setDrawerListener(mActionBarDrawerToggle)

        mActionBarDrawerToggle.syncState()//ActionBarDrawerToggle与DrawerLayout的状态同步，
        // 并将ActionBarDrawerToggle中的drawer图标，设置为ActionBar的Home-Button的icon
        //设置侧拉菜单栏
        mNavView.apply {
            itemTextColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.ThemeTextColor
                )
            )
            itemIconTintList = null

            setNavigationItemSelectedListener { item ->
                Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_LONG).show()
                when (item.itemId) {
                    R.id.item_bind -> this@MainActivity.startActivity<BindActivity>()
                    R.id.item_reservation -> this@MainActivity.startActivity<MyReservationActivity>()
                    R.id.item_logout
                    -> {
                        CommonPreferences.clear()
                        this@MainActivity.startActivity<LoginActivity>()
                        finish()
                    }

                }
                mDlNavButtom.closeDrawers()
                item.isCheckable = false // 设置选中后的阴影取消
                true
            }
        }
    }


}
