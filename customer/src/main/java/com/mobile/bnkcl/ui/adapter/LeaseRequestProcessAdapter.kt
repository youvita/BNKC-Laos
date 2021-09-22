package com.mobile.bnkcl.ui.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationData
import com.mobile.bnkcl.databinding.ItemLeaseRequestProcessBinding

class LeaseRequestProcessAdapter :
    BaseAdapter<ItemLeaseRequestProcessBinding, LeaseApplicationData, LeaseRequestProcessAdapter.ViewHolder>() {

    private var type: Int? = null
    private lateinit var context: Context
    private var productTypeList: ArrayList<CodesData> = ArrayList()
    private var progressTypeList: ArrayList<CodesData> = ArrayList()
    private var listener: CloseClickedListener? = null

    /**
     * 1 = application
     * 2 = screening
     * 3 = result
     */
    fun setLeaseProcessType(type: Int) {
        this.type = type
    }

    fun setListener(listener: CloseClickedListener) {
        this.listener = listener
    }

    fun setProductTypeList(list: ArrayList<CodesData>) {
        this.productTypeList = list
    }

    fun setProgressTypeList(list: ArrayList<CodesData>) {
        this.progressTypeList = list
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_lease_request_process
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        context = parent.context
        return ViewHolder(binding)
    }

    interface CloseClickedListener {
        fun onCloseClicked()
    }

    override fun setBindData(holder: ViewHolder, data: LeaseApplicationData, position: Int) {
        binding.leaseApplication = data
        binding.ltdAppliedInfo.leaseApplication = data

        for (i in 0 until productTypeList.size - 1) {
            if (data.productType.equals(
                    productTypeList[i].code,
                    ignoreCase = true
                )
            ) binding.ltdAppliedInfo.tvProductType.text = productTypeList[i].title
        }

        binding.btnClose.setOnClickListener {
            listener?.onCloseClicked()
        }

        when (type) {
            1 -> {
                binding.llApplicationResult.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE
                binding.tvPhaseOfProgress.text = progressTypeList[0].title

                binding.tvTitle.text = context.getString(R.string.progress_application)
            }
            2 -> {
                binding.llApplicationResult.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE
                binding.tvPhaseOfProgress.text = progressTypeList[1].title

                binding.tvTitle.text = context.getString(R.string.progress_screening)
            }
            3 -> {
                binding.tvTitle.text = context.getString(R.string.progress_result)
                binding.llApplicationResult.visibility = View.GONE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                if (data.progressStatus == progressTypeList[2].code) {
                    binding.tvApproval.text = progressTypeList[2].title
                    binding.llApprovalResult.visibility = View.VISIBLE
                    binding.llRejectResult.visibility = View.GONE
                } else if (data.progressStatus == progressTypeList[3].code){
                    binding.tvReject.text = progressTypeList[3].title
                    binding.llApprovalResult.visibility = View.GONE
                    binding.llRejectResult.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * provide a reference to the type of views that you are using custom view holder
     * @param binding item binding
     */
    class ViewHolder(binding: ItemLeaseRequestProcessBinding) :
        RecyclerView.ViewHolder(binding.root)
}