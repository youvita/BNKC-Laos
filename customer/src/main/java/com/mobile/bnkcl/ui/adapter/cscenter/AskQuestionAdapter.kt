package com.mobile.bnkcl.ui.adapter.cscenter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.cscenter.ClaimItems
import com.mobile.bnkcl.databinding.ItemCsQuestionBinding

class AskQuestionAdapter: BaseAdapter<ItemCsQuestionBinding, ClaimItems, AskQuestionAdapter.ViewHolder>() {

    private var itemClickListener: ((id: Int?, category: String?) -> Unit)? = null

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_cs_question
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: ClaimItems, position: Int) {
         binding.item   = data
         binding.action = this@AskQuestionAdapter
    }

    /**
     * binding method
     */
    fun onItemClick(item: ClaimItems?) {
        itemClickListener?.invoke(item?.id, item?.category)
    }

    /**
     * item listener
     */
    fun setOnItemClickListener(itemClickListener: ((id: Int?, category: String?) -> Unit)) = apply {
        this.itemClickListener = itemClickListener
    }

    /**
     * provide a reference to the type of views that you are using custom view holder
     * @param binding item binding
     */
    class ViewHolder(val binding: ItemCsQuestionBinding) : RecyclerView.ViewHolder(binding.root)

}