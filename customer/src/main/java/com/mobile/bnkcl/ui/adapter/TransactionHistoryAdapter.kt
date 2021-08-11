package com.mobile.bnkcl.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.ItemCommentBinding
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.databinding.ItemTransactionHistoryLayoutBinding

/**
 *
 * @author chan youvita
 * @since 2021.04.24
 *
 */
class TransactionHistoryAdapter : BaseAdapter<ItemTransactionHistoryLayoutBinding, CommentsItem, TransactionHistoryAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_transaction_history_layout
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: CommentsItem, position: Int) {
        binding.item = data
    }

    /**
     * provide a reference to the type of views that you are using custom view holder
     * @param binding item binding
     */
    class ViewHolder(binding: ItemTransactionHistoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}