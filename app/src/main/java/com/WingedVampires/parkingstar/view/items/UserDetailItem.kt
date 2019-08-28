package com.WingedVampires.parkingstar.view.items

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.ui.rec.Item
import com.WingedVampires.parkingstar.commons.ui.rec.ItemController
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.layoutInflater

enum class UserDetailType {
    EDIT, SPINNER, TEXT
}

class UserDetailItem<T>(
    val context: Context,
    val picId: Int,
    val title: String,
    val userDetailType: UserDetailType,
    val value: T,
    val spinnerList: List<String> = mutableListOf()
) : Item {
    var mValue: T? = null
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view =
                parent.context.layoutInflater.inflate(R.layout.item_user_detail, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as UserDetailItem<*>

            val value = item.value

            holder.apply {
                pic.imageResource = item.picId
                title.text = item.title
                when (item.userDetailType) {
                    UserDetailType.TEXT -> {
                        textContent.visibility = View.VISIBLE
                        if (value is String) textContent.text = value
                    }
                    UserDetailType.EDIT -> {
                        editContent.apply {
                            visibility = View.VISIBLE
                            if (value is String) setText(value)
                            editContent.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    count: Int,
                                    after: Int
                                ) = Unit

                                override fun afterTextChanged(s: Editable?) = Unit

                                override fun onTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    before: Int,
                                    count: Int
                                ) {

                                    if (editContent.text.isNotBlank()) {
                                        if (item.mValue is String) {
                                            item as UserDetailItem<String>
                                            item.mValue = editContent.text.toString()
                                        }

                                    }
                                }
                            })

                        }
                    }
                    UserDetailType.SPINNER -> {
                        spinnerContent.apply {
                            visibility = View.VISIBLE
                            adapter = ArrayAdapter(
                                item.context,
                                R.layout.custom_spiner_text_item,
                                item.spinnerList
                            )
                            onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        if (item.value is Int) {
                                            item as UserDetailItem<Int>
                                            item.mValue = position
                                        }

                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                                }
                        }

                    }
                }
            }
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val pic: ImageView = itemView.findViewById(R.id.iv_user_label)
            val title: TextView = itemView.findViewById(R.id.tv_user_title)
            val editContent: EditText = itemView.findViewById(R.id.et_user_content)
            val spinnerContent: Spinner = itemView.findViewById(R.id.sp_user_content)
            val textContent: TextView = itemView.findViewById(R.id.tv_user_content)
        }
    }
}
