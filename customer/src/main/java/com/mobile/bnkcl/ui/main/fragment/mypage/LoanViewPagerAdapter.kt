package com.mobile.bnkcl.ui.main.fragment.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.MyLoan
import com.mobile.bnkcl.databinding.LoanItemLayoutBinding
import java.util.*

class LoanViewPagerAdapter(listener: LoanPagerClickedListener) :
    RecyclerView.Adapter<LoanViewPagerAdapter.ViewHolder>() {
    private val mLoanItemList: ArrayList<MyLoan> = ArrayList<MyLoan>()
    private val mListener: LoanPagerClickedListener = listener
    fun addData(list: ArrayList<MyLoan?>) {
        mLoanItemList.clear()
        list.add(MyLoan())
//        mLoanItemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: LoanItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.loan_item_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item: MyLoan = mLoanItemList[position]
        holder.setBinding(item, position)
        holder.binding.btnBillPayment.setOnClickListener(View.OnClickListener {
            mListener.onBillPaymentClicked(
                item.contractNo,
                position
            )
        })
        holder.binding.btnManagement.setOnClickListener(View.OnClickListener {
            mListener.onManagementClicked(
                item.contractNo,
                position
            )
        })
        if (position == mLoanItemList.size - 1) {
            holder.binding.btnAddLoan.setOnClickListener(View.OnClickListener { mListener.onAddNewLoanClicked() })
        }
    }

    override fun getItemCount(): Int {
        return mLoanItemList.size
    }

    inner class ViewHolder(binding: LoanItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: LoanItemLayoutBinding = binding
        fun setBinding(item: MyLoan?, position: Int) {
//            binding.setLoanItem(item)

            // Show at new loan at last position
            if (position == mLoanItemList.size - 1) {
                binding.llLoanItem.visibility = View.GONE
                binding.rlAddNewLoan.visibility = View.VISIBLE
            } else {
                binding.llLoanItem.visibility = View.VISIBLE
                binding.rlAddNewLoan.visibility = View.GONE
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