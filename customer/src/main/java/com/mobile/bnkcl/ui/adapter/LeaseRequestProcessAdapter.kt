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

    /**
     * 1 = application
     * 2 = screening
     * 3 = approval lease
     * 4 = reject lease
     */
    fun setLeaseProcessType(type: Int) {
        this.type = type
    }

    fun setProductTypeList(list: ArrayList<CodesData>) {
        this.productTypeList = list
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_lease_request_process
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        context = parent.context
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: LeaseApplicationData, position: Int) {
        binding.leaseApplication = data
        binding.ltdAppliedInfo.leaseApplication = data

        for (i in 0 until productTypeList.size - 1) {
            if (data.productType.equals(productTypeList[i].code, ignoreCase = true)) binding.ltdAppliedInfo.tvProductType.text = productTypeList[i].title
        }

        when (type) {
            1 -> {
                binding.llApplicationResult.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                binding.tvTitle.text = context.getString(R.string.progress_application)
            }
            2 -> {
                binding.llApplicationResult.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                binding.tvTitle.text = context.getString(R.string.progress_screening)
            }
            3 -> {
                binding.llApplicationResult.visibility = View.GONE
                binding.llApprovalResult.visibility = View.VISIBLE
                binding.llRejectResult.visibility = View.GONE

                binding.tvTitle.text = context.getString(R.string.progress_result)
            }
            4 -> {
                binding.llApplicationResult.visibility = View.GONE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.VISIBLE

                binding.tvTitle.text = context.getString(R.string.progress_result)
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