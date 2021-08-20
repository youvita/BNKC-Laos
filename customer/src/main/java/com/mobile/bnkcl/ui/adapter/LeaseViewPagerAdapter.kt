package com.mobile.bnkcl.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.databinding.LeaseItemLayoutBinding
import java.util.*

class LeaseViewPagerAdapter(listener: LoanPagerClickedListener) :
    RecyclerView.Adapter<LeaseViewPagerAdapter.ViewHolder>() {

    private val myLeaseItemList: ArrayList<MyLeasesData> = ArrayList<MyLeasesData>()
    private val mListener: LoanPagerClickedListener = listener

    fun addData(list: ArrayList<MyLeasesData>) {
        myLeaseItemList.clear()
        list.add(MyLeasesData())
        myLeaseItemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: LeaseItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lease_item_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item: MyLeasesData = myLeaseItemList[position]
        holder.setBinding(item, position)
        holder.binding.btnBillPayment.setOnClickListener {
            mListener.onBillPaymentClicked(
                item.contractNo,
                position
            )
        }
        holder.binding.btnManagement.setOnClickListener {
            mListener.onManagementClicked(
                item.contractNo,
                position
            )
        }
        if (position == myLeaseItemList.size - 1) {
            holder.binding.btnAddLease.setOnClickListener { mListener.onAddNewLoanClicked() }
        }
    }

    override fun getItemCount(): Int {
        return myLeaseItemList.size
    }

    inner class ViewHolder(val binding: LeaseItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setBinding(item: MyLeasesData, position: Int) {
            binding.leaseItem = item

            // Show at new lease at last position
            if (position == myLeaseItemList.size - 1) {
                binding.llLeaseItem.visibility = View.GONE
                binding.rlAddNewLease.visibility = View.VISIBLE
            } else {
                binding.llLeaseItem.visibility = View.VISIBLE
                binding.rlAddNewLease.visibility = View.GONE
            }
            binding.executePendingBindings()
        }

    }

    interface LoanPagerClickedListener {
        fun onBillPaymentClicked(contractNo: String?, position: Int)
        fun onManagementClicked(contractNo: String?, position: Int)
        fun onAddNewLoanClicked()
    }

    companion object {
        fun getLoanTypeName(context: Context, type: String?): String {
            return when (type) {
                "LAT001" -> context.resources.getString(R.string.loan_type_001)
                "LAT002" -> context.getString(R.string.loan_type_002)
                "LAT003" -> context.getString(R.string.loan_type_003)
                else -> ""
            }
        }
    }

}