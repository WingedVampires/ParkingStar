package com.WingedVampires.parkingstar.view.items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.ItemController
import com.WingedVampires.parkingstar.model.ParkingUtils
import com.WingedVampires.parkingstar.view.activities.ReservationActivity
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity

class MyreservationItem(
    val parkingId: String,
    val parking: String,
    val car: String,
    val time: String,
    val showDelete: Boolean,
    val block: (MyreservationItem) -> Unit
) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view =
                parent.context.layoutInflater.inflate(R.layout.item_myreservation, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as MyreservationItem
            holder as ViewHolder

            holder.apply {
                parking.text = item.parking
                car.text = item.car
                time.text = item.time

                delete.setOnClickListener { item.block(item) }

                if (item.showDelete) {
                    delete.visibility = View.VISIBLE
                    itemView.setOnClickListener {
                        it.context.startActivity<ReservationActivity>(ParkingUtils.PAKING_INDEX to item.parkingId)
                    }
                }
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val delete: ImageView = itemView.findViewById(R.id.iv_myreservation_delete)
        val parking: TextView = itemView.findViewById(R.id.tv_myreservation_parking)
        val car: TextView = itemView.findViewById(R.id.tv_myservation_car)
        val time: TextView = itemView.findViewById(R.id.tv_myreservation_time)
    }
}

fun MutableList<Item>.myreservationItem(
    parkingId: String,
    parking: String,
    car: String,
    time: String,
    showDelete: Boolean = false,
    block: (MyreservationItem) -> Unit = {}
) = add(MyreservationItem(parkingId, parking, car, time, showDelete, block))