package com.WingedVampires.parkingstar.view.items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.ItemController
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.layoutInflater

class ParkingCarItem(val num: Int, val isPre: Boolean, val block: (View, ViewHolder) -> Unit) :
    Item {

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view =
                parent.context.layoutInflater.inflate(R.layout.item_parking_car, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ParkingCarItem

            holder.apply {
                if (item.isPre) {
                    add.visibility = View.GONE
                    not.visibility = View.VISIBLE
                    pic.backgroundResource = R.drawable.cd_car4
                }
                add.setOnClickListener { item.block(it, this) }
                position.text = "${item.num}号车位"
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic: ImageView = itemView.findViewById(R.id.iv_parking_car)
        val add: ImageView = itemView.findViewById(R.id.iv_parking_add)
        val not: TextView = itemView.findViewById(R.id.tv_parking_not)
        val position: TextView = itemView.findViewById(R.id.tv_parking_num)
    }
}

fun MutableList<Item>.parkingCarItem(
    num: Int,
    isPre: Boolean,
    block: (View, ViewHolder: ParkingCarItem.ViewHolder) -> Unit = { _, _ -> }
) =
    add(ParkingCarItem(num, isPre, block))