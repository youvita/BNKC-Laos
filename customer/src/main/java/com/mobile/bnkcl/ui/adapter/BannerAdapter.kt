package com.mobile.bnkcl.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.BannerItemLayoutBinding

class BannerAdapter : BaseAdapter<BannerItemLayoutBinding, Int, BannerAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_item_layout
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: Int, position: Int) {
        binding.bannerImage = data
    }

    /**
     * provide a reference to the type of views that you are using custom view holder
     * @param binding item binding
     */
    class ViewHolder(binding: BannerItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}