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

class MyTitleItem(val title: String, val block: (ViewHolder, MyTitleItem) -> Unit) : Item {
    var isOpen = false

    override fun areContentsTheSame(newItem: Item): Boolean {
        return title == (newItem as? MyTitleItem)?.title
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return title == (newItem as? MyTitleItem)?.title
    }

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view =
                parent.context.layoutInflater.inflate(R.layout.item_mytitle, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as MyTitleItem
            holder as ViewHolder

            holder.apply {
                title.text = item.title
                more.setImageResource(R.drawable.cd_show)
                more.setOnClickListener { item.block(holder, item) }
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val more: ImageView = itemView.findViewById(R.id.iv_mytitle_more)
        val title: TextView = itemView.findViewById(R.id.tv_mytitle_title)
    }
}

fun MutableList<Item>.myTitleItem(
    title: String,
    block: (MyTitleItem.ViewHolder, MyTitleItem) -> Unit = { _, _ -> }
) = add(MyTitleItem(title, block))