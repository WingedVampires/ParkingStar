package com.WingedVampires.parkingstar.view.items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class ParkingItem(
    val title: String,
    val position: String,
    val num: String,
    val isReservation: Boolean,
    val block: (View) -> Unit
) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_parking, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ParkingItem

            holder.apply {
                enter.setOnClickListener { item.block(it) }
                title.text = item.title
                position.text = item.position
                num.text = item.num
                reservation.text = if (item.isReservation) "可以预约哦" else "现在还不支持预约yo!"
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic: ImageView = itemView.findViewById(R.id.iv_parking_pic)
        val enter: ImageView = itemView.findViewById(R.id.iv_parking_enter)
        val title: TextView = itemView.findViewById(R.id.tv_parking_title)
        val position: TextView = itemView.findViewById(R.id.tv_parking_position)
        val num: TextView = itemView.findViewById(R.id.tv_parking_num)
        val reservation: TextView = itemView.findViewById(R.id.tv_parking_reservation)
    }
}

fun MutableList<Item>.parkingItem(
    title: String,
    position: String,
    num: String,
    isReservation: Boolean,
    block: (View) -> Unit = {}
) = add(ParkingItem(title, position, num, isReservation, block))