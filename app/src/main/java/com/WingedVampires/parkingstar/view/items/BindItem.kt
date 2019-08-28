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

class BindItem(val number: String, val block: (View) -> Unit) : Item {

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view =
                parent.context.layoutInflater.inflate(R.layout.item_bind, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as BindItem

            holder.apply {
                number.text = item.number
                delete.setOnClickListener {
                    item.block(it)
                }
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val delete: ImageView = itemView.findViewById(R.id.iv_bind_delete)
        val number: TextView = itemView.findViewById(R.id.tv_bind_number)
    }
}

fun MutableList<Item>.bindItem(number: String, block: (View) -> Unit) = add(BindItem(number, block))