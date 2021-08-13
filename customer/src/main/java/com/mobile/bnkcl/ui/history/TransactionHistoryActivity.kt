package com.mobile.bnkcl.ui.history

import android.os.Bundle
import android.view.View
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.ActivityTransactionHistoryBinding
import com.mobile.bnkcl.ui.adapter.TransactionHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionHistoryActivity : BaseActivity<ActivityTransactionHistoryBinding>() {

    @Inject
    lateinit var transactionAdapter: TransactionHistoryAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_transaction_history
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()

    }

    private fun initAdapter() {

        val list = mutableListOf<CommentsItem>()

        val item = CommentsItem()
        item.postId = 1
        item.id = 1
        item.name = "Charity Plan"
        item.email = "email"
        item.body = "body"

        for (i in 0..14) {
            list.add(i, item)
        }

        binding.transactionRecyclerview.adapter = transactionAdapter
        transactionAdapter.addItemList(list)

        if (list.size == 0) {
            binding.llWrapSort.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }

    }


}