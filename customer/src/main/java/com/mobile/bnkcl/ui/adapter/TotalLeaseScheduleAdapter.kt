package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.ItemTotalLeaseScheduleLayoutBinding
import com.mobile.bnkcl.utilities.UtilAnimation

class TotalLeaseScheduleAdapter : BaseAdapter<ItemTotalLeaseScheduleLayoutBinding, CommentsItem, TotalLeaseScheduleAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_total_lease_schedule_layout
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: CommentsItem, position: Int) {
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
    class ViewHolder(val binding: ItemTotalLeaseScheduleLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}