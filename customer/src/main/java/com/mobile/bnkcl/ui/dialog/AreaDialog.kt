package com.mobile.bnkcl.ui.dialog

import android.content.Context
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogHomeAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AreaDialog  : BaseDialogFragment<DialogHomeAddressBinding>{
    constructor(context: Context, title: String, obj : ArrayList<Any>, selectedItem : Int, listener : onItemClickLisener) : super(){
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_home_address
    }



    interface onItemClickLisener{
        fun onClickItem()
    }
}