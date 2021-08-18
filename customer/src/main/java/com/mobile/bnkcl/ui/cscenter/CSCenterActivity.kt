package com.mobile.bnkcl.ui.cscenter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.response.cscenter.ClaimItems
import com.mobile.bnkcl.databinding.ActivityCSCenterBinding
import com.mobile.bnkcl.ui.adapter.cscenter.AskQuestionAdapter
import com.mobile.bnkcl.ui.cscenter.viewmodel.CSCenterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class CSCenterActivity : BaseActivity<ActivityCSCenterBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int= R.layout.activity_c_s_center

    private val csCenterViewModel : CSCenterViewModel by viewModels()
    private lateinit var claimDataRequest : ClaimDataRequest
    private lateinit var claimItemsList : ArrayList<ClaimItems>
    private lateinit var adapter : AskQuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        claimDataRequest = ClaimDataRequest()
        claimItemsList = ArrayList()
        adapter = AskQuestionAdapter(this)

        binding.tvAskBnk.setOnClickListener(this)
    }

    private fun getClaimData(){
        try {
            csCenterViewModel.getClaimData(claimDataRequest)
            csCenterViewModel.claim.observe(this){
                if (it != null) {
                    claimItemsList.addAll(claimItemsList)
                    adapter.addItemList(claimItemsList)
                    adapter.notifyDataSetChanged()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_ask_bnk ->{getClaimData()}
        }
    }
}