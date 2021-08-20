package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryData
import com.mobile.bnkcl.databinding.ItemTransactionHistoryLayoutBinding
import com.mobile.bnkcl.utilities.UtilAnimation

class TransactionHistoryAdapter :
    BaseAdapter<ItemTransactionHistoryLayoutBinding, TransactionHistoryData, TransactionHistoryAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_transaction_history_layout
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: TransactionHistoryData, position: Int) {
        binding.item = data
        holder.binding.llItem.setOnClickListener {
            if (holder.binding.llExpandInfo.visibility == View.VISIBLE) {
                UtilAnimation.collapse(holder.binding.llExpandInfo, 300)
                holder.binding.ivItemImage.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(holder.binding.llExpandInfo, 300)
                holder.binding.ivItemImage.setImageResource(R.drawable.ic_fold_ico)
            }
        }
    }

    /**
     * provide a reference to the type of views that you are using custom view holder
     * @param binding item binding
     */
    class ViewHolder(val binding: ItemTransactionHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}