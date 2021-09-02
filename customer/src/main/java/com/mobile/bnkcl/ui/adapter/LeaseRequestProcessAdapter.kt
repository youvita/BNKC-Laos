package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationData
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationResponse
import com.mobile.bnkcl.databinding.ItemLeaseRequestProcessBinding

class LeaseRequestProcessAdapter :
    BaseAdapter<ItemLeaseRequestProcessBinding, LeaseApplicationData, LeaseRequestProcessAdapter.ViewHolder>() {

    var type: Int? = null

    /**
     * 1 = application
     * 2 = screening
     * 3 = approval lease
     * 4 = reject lease
     */
    fun setLeaseProcessType(type: Int) {
        this.type = type
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_lease_request_process
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: LeaseApplicationData, position: Int) {
        binding.leaseApplication = data
        binding.ltdAppliedInfo.leaseApplication = data
        when (type) {
            1 -> {
                binding.llApplicationResult.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                binding.tvPhaseOfProgress.text = "wait for screening"
                binding.tvTitle.text = "Application"
            }
            2 -> {
                binding.llApplicationResult.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                binding.tvPhaseOfProgress.text = "wait for result"
                binding.tvTitle.text = "Screening"
            }
            3 -> {
                binding.llApplicationResult.visibility = View.GONE
                binding.llApprovalResult.visibility = View.VISIBLE
                binding.llRejectResult.visibility = View.GONE

                binding.tvTitle.text = "Result of Screening"
            }
            4 -> {
                binding.llApplicationResult.visibility = View.GONE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.VISIBLE
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