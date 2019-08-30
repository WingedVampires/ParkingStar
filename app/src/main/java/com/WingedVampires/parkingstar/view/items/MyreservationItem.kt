package com.WingedVampires.parkingstar.view.items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.ItemController

class MyreservationItem : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val delete: ImageView = itemView.findViewById(R.id.iv_bind_delete)
        val number: TextView = itemView.findViewById(R.id.tv_bind_number)
    }
}