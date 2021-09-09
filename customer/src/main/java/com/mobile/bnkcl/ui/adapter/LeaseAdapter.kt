package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.databinding.ItemLeaseLayoutBinding
import java.util.*
import kotlin.collections.ArrayList

class LeaseAdapter(listener: LeaseItemClickedListener) :
    BaseAdapter<ItemLeaseLayoutBinding, MyLeasesData, LeaseAdapter.ViewHolder>() {

    private val myLeaseItemList: ArrayList<MyLeasesData> = ArrayList()
    private var productTypeList: ArrayList<CodesData> = ArrayList()
    private val mListener: LeaseItemClickedListener = listener

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_lease_layout
    }

    fun setProductTypeList(list: ArrayList<CodesData>) {
        productTypeList = list
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: MyLeasesData, position: Int) {
        binding.leaseItem = data
        holder.setBinding(data, position)

        for (i in 0 until productTypeList.size - 1) {
            if (data.leaseType.equals(productTypeList[i].code, ignoreCase = true)) holder.binding.tvProductType.text = productTypeList[i].title
        }

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

    inner class ViewHolder(val binding: ItemLeaseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
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