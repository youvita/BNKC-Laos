package com.mobile.bnkcl.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.notice.NoticeRequest
import com.mobile.bnkcl.data.response.notice.NoticeDataResponse
import com.mobile.bnkcl.data.response.notice.NoticeItems
import com.mobile.bnkcl.databinding.ItemNoticeBinding

class NoticeAdapter : com.bnkc.sourcemodule.base.BaseAdapter<ItemNoticeBinding,NoticeDataResponse,NoticeAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_notice
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: NoticeDataResponse, position: Int) {
        binding.viewModel = data
    }

    class ViewHolder(binding: ItemNoticeBinding) :RecyclerView.ViewHolder(binding.root)


}