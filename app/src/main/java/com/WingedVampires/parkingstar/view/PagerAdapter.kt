package com.WingedVampires.parkingstar.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.ViewGroup


class PagerAdapter(fm: FragmentManager, val appCompatActivity: AppCompatActivity) :
    FragmentPagerAdapter(fm) {

    private var mCurrentfragment: Fragment? = null
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()
    private val images = mutableListOf<Int>()

    fun add(fragment: Fragment, title: String, image: Int) {
        fragments.add(fragment)
        titles.add(title)
        images.add(image)
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        val image = appCompatActivity.resources.getDrawable(images[position])
        image.setBounds(0, 0, image.intrinsicWidth / 6, image.intrinsicHeight / 6)
        val imageSpan = ImageSpan(image, ImageSpan.ALIGN_BOTTOM)

        val ss = SpannableString(" \n" + titles[position])
        ss.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss

//        return titles[position]
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        mCurrentfragment = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }

    fun getCurrentFragment(): Fragment? = mCurrentfragment
}