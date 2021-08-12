package com.mobile.bnkcl.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.ActivityTransactionHistoryBinding
import com.mobile.bnkcl.ui.adapter.TransactionHistoryAdapter
import javax.inject.Inject

class TransactionHistoryActivity : BaseActivity<ActivityTransactionHistoryBinding>() {

    @Inject
    lateinit var transactionAdapter: TransactionHistoryAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_transaction_history
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transactionAdapter = TransactionHistoryAdapter()
        val list = mutableListOf<CommentsItem>()

        val item = CommentsItem()
        item.postId = 1
        item.id = 1
        item.name = "dara"
        item.email = "email"
        item.body = "body"

        list.add(0, item)
        list.add(1, item)

        binding.transactionRecyclerview.adapter = transactionAdapter
        transactionAdapter.addItemList(list)

        Log.d(">>>", "onCreate: " + transactionAdapter.itemCount)
        if (list.size == 0) {
            binding.llWrapSort.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }
    }

}