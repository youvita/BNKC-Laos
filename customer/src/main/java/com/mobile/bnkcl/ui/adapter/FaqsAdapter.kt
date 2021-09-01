package com.mobile.bnkcl.ui.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.faq.FaqItemsRes
import com.mobile.bnkcl.databinding.ItemFaqsBinding
import com.mobile.bnkcl.utilities.UtilAnimation

class FaqsAdapter : BaseAdapter<ItemFaqsBinding,FaqItemsRes,FaqsAdapter.ViewHolder>(){
    lateinit var context: Context

    class ViewHolder(val binding: ItemFaqsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_faqs
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        context = parent.context
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: FaqItemsRes, position: Int) {

        binding.item = data
        binding.title.text = data.title

        binding.llMoreLeaseInfo.setOnClickListener {
            Log.d(">>>", "click: ")

            if (binding.llWrapLeaseInfo.visibility == View.VISIBLE) {
                UtilAnimation.collapse(binding.llWrapLeaseInfo, 300)
                binding.ivMoreLeaseInfo.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(binding.llWrapLeaseInfo, 300)
                binding.ivMoreLeaseInfo.setImageResource(R.drawable.ic_fold_ico)
            }

        }
    }



}