package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.ItemLeaseRequestProcessBinding

class LeaseRequestProcessAdapter :
    BaseAdapter<ItemLeaseRequestProcessBinding, CommentsItem, LeaseRequestProcessAdapter.ViewHolder>() {

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

    override fun setBindData(holder: ViewHolder, data: CommentsItem, position: Int) {
        binding.viewModel = data
        when (type) {
            1 -> {
                binding.llApplicationNScreening.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                binding.tvPhaseOfProgress.text = "wait for screening"
            }
            2 -> {
                binding.llApplicationNScreening.visibility = View.VISIBLE
                binding.llApprovalResult.visibility = View.GONE
                binding.llRejectResult.visibility = View.GONE

                binding.tvPhaseOfProgress.text = "wait for result"
            }
            3 -> {
                binding.llApplicationNScreening.visibility = View.GONE
                binding.llApprovalResult.visibility = View.VISIBLE
                binding.llRejectResult.visibility = View.GONE
            }
            4 -> {
                binding.llApplicationNScreening.visibility = View.GONE
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