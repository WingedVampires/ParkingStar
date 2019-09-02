package com.WingedVampires.parkingstar.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.WingedVampires.parkingstar.R
import com.WingedVampires.parkingstar.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.WingedVampires.parkingstar.commons.experimental.extensions.awaitAndHandle
import com.WingedVampires.parkingstar.commons.ui.rec.withItems
import com.WingedVampires.parkingstar.model.ParkingService
import com.WingedVampires.parkingstar.view.items.UserDetailItem
import com.WingedVampires.parkingstar.view.items.UserDetailType
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.imageResource

class UserFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }
    private var isEdit = false
    lateinit var edit: ImageView
    lateinit var nrealName: UserDetailItem
    lateinit var ngender: UserDetailItem
    lateinit var nphone: UserDetailItem
    lateinit var nidentityNumber: UserDetailItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        recyclerView = view.findViewById(R.id.rv_user_items)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        edit = view.findViewById(R.id.iv_user_edit)
        val refresh = view.findViewById<ImageView>(R.id.iv_user_refresh)

        loadAndRefresh()

        edit.setOnClickListener {
            if (isEdit) {
                commit()
                loadAndRefresh()
                isEdit = false
                refresh.visibility = View.VISIBLE
            } else {
                editPerson()
                refresh.visibility = View.GONE
                isEdit = true
            }
        }
        refresh.setOnClickListener { loadAndRefresh() }
        return view
    }

    private fun loadAndRefresh() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            itemManager.refreshAll { }
            val result = ParkingService.listAllInfoOfUser().awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@UserFragment.context!!, "加载失败", Toast.LENGTH_SHORT).show()
            } ?: return@launch

            Toasty.success(this@UserFragment.context!!, result.message, Toast.LENGTH_SHORT).show()

            val userInfo = result.data?.get(0) ?: return@launch
            edit.imageResource = R.drawable.cd_person_edit
            val realName = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_person,
                "Actual Name",
                UserDetailType.TEXT,
                userInfo.real_name ?: ""
            )

            val gender = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_sex,
                "Gender",
                UserDetailType.TEXT,
                userInfo.gender ?: ""
            )

            val phone = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_phone,
                "Phone",
                UserDetailType.TEXT,
                userInfo.phone ?: ""
            )

            val identityNumber = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_card,
                "Identity Number",
                UserDetailType.TEXT,
                userInfo.chinaId ?: ""
            )

            itemManager.refreshAll {
                add(realName)
                add(gender)
                add(phone)
                add(identityNumber)
            }
        }
    }

    private fun editPerson() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            itemManager.refreshAll { }
            val result = ParkingService.listAllInfoOfUser().awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@UserFragment.context!!, "加载失败", Toast.LENGTH_SHORT).show()
            } ?: return@launch

            Toasty.success(this@UserFragment.context!!, result.message, Toast.LENGTH_SHORT).show()
            val userInfo = result.data?.get(0) ?: return@launch
            edit.imageResource = R.drawable.cd_righr
            nrealName = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_person,
                "Actual Name",
                UserDetailType.EDIT,
                userInfo.real_name ?: ""
            )

            ngender = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_sex,
                "Gender",
                UserDetailType.EDIT,
                userInfo.gender ?: ""
            )

            nphone = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_phone,
                "Phone",
                UserDetailType.EDIT,
                userInfo.phone ?: ""
            )

            nidentityNumber = UserDetailItem(
                this@UserFragment.context!!,
                R.drawable.cd_card,
                "Identity Number",
                UserDetailType.EDIT,
                userInfo.chinaId ?: ""
            )

            itemManager.refreshAll {
                add(nrealName)
                add(ngender)
                add(nphone)
                add(nidentityNumber)
            }
        }
    }

    private fun commit() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val result = ParkingService.addInfo(
                ngender.mValue,
                nphone.mValue,
                nidentityNumber.mValue,
                nrealName.mValue
            ).awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@UserFragment.context!!, "加载失败", Toast.LENGTH_SHORT).show()
            } ?: return@launch

            Toasty.success(this@UserFragment.context!!, result.message, Toast.LENGTH_SHORT).show()

        }

        loadAndRefresh()
    }

}