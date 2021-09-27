package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.databinding.ItemLeaseLayoutBinding

class LeaseAdapter :
    BaseAdapter<ItemLeaseLayoutBinding, MyLeasesData, LeaseAdapter.ViewHolder>() {

    private var productTypeList: ArrayList<CodesData> = ArrayList()
    private var mListener: LeaseItemClickedListener? = null

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_lease_layout
    }

    fun setProductTypeList(list: ArrayList<CodesData>) {
        productTypeList.clear()
        productTypeList = list
    }

    fun setItemClickListener(listener: LeaseItemClickedListener) {
        this.mListener = listener
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: MyLeasesData, position: Int) {
        binding.leaseItem = data
        holder.setBinding(data, position)

        for (i in 0 until productTypeList.size - 1) {
            if (data.leaseType.equals(
                    productTypeList[i].code,
                    ignoreCase = true
                )
            ) holder.binding.tvProductType.text = productTypeList[i].title
        }

        holder.binding.btnBillPayment.setOnClickListener {
            if (!data.contractNo.isNullOrEmpty()) {
                mListener!!.onBillPaymentClicked(
                    data.contractNo,
                    position
                )
            }
        }
        holder.binding.btnManagement.setOnClickListener {
            if (!data.contractNo.isNullOrEmpty()) {
                mListener!!.onManagementClicked(
                    data.contractNo,
                    position
                )
            }
        }

        holder.binding.llAddNewLease.setOnClickListener {
            mListener!!.onAddNewLeaseClicked()
        }

        holder.binding.btnAddLease.setOnClickListener {
            mListener!!.onAddNewLeaseClicked()
        }
    }

    inner class ViewHolder(val binding: ItemLeaseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setBinding(item: MyLeasesData, position: Int) {
            binding.leaseItem = item

            binding.llAddNewLease.visibility =
                if (position == itemCount - 1) View.VISIBLE else View.GONE
            binding.llLeaseItem.visibility =
                if (position == itemCount - 1) View.GONE else View.VISIBLE

            binding.executePendingBindings()
        }
    }

    interface LeaseItemClickedListener {
        fun onBillPaymentClicked(contractNo: String?, position: Int)
        fun onManagementClicked(contractNo: String?, position: Int)
        fun onAddNewLeaseClicked()
    }
}