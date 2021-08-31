package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.databinding.ItemLeaseLayoutBinding
import java.util.*

class LeaseAdapter(listener: LeaseItemClickedListener): BaseAdapter<ItemLeaseLayoutBinding, MyLeasesData, LeaseAdapter.ViewHolder>() {

    private val myLeaseItemList: ArrayList<MyLeasesData> = ArrayList<MyLeasesData>()
    private val mListener: LeaseItemClickedListener = listener

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_lease_layout
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: MyLeasesData, position: Int) {
        binding.leaseItem = data
        holder.setBinding(data, position)

        holder.binding.btnBillPayment.setOnClickListener {
            mListener.onBillPaymentClicked(
                data.contractNo,
                position
            )
        }
        holder.binding.btnManagement.setOnClickListener {
            mListener.onManagementClicked(
                data.contractNo,
                position
            )
        }
        if (position == myLeaseItemList.size - 1) {
            holder.binding.btnAddLease.setOnClickListener { mListener.onAddNewLeaseClicked() }
        }
    }

    inner class ViewHolder(val binding: ItemLeaseLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun setBinding(item: MyLeasesData, position: Int) {
            binding.leaseItem = item

            // Show at new lease at last position
            if (position == itemCount - 1) {
                binding.llLeaseItem.visibility = View.GONE
                binding.llAddNewLease.visibility = View.VISIBLE
            } else {
                binding.llLeaseItem.visibility = View.VISIBLE
                binding.llAddNewLease.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
    }

    interface LeaseItemClickedListener {
        fun onBillPaymentClicked(contractNo: String?, position: Int)
        fun onManagementClicked(contractNo: String?, position: Int)
        fun onAddNewLeaseClicked()
    }
}