package com.mobile.bnkcl.com.alert

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.area.AreaObjResponse
import com.mobile.bnkcl.data.response.area.CapitalData
import com.mobile.bnkcl.databinding.DialogHomeAddressBinding
import com.mobile.bnkcl.ui.adapter.DlgAreaAdapter
import com.mobile.bnkcl.utilities.Utils
import java.util.*
import kotlin.collections.ArrayList

class DlgArea(context: Context,title: String,obj : ArrayList<Any>, selectedItem : Int,
              listener : onItemClickLisener, cancel_able : Boolean) : BaseDialogFragment<DialogHomeAddressBinding>() {




    override fun getLayoutId(): Int {
        return R.layout.dialog_home_address
    }


    interface onItemClickLisener {
        fun onClickItem(dialogIndex : Int, obj: Any)

    }
}