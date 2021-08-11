package com.mobile.bnkcl.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
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
        val arrayList = ArrayList<CommentsItem>()

        binding.transactionRecyclerview.adapter = transactionAdapter

        val item1 = CommentsItem(1,1,"Jam","jamm@gmail.com","hello this is me, Jam!")
        arrayList.add(item1)
        arrayList.add(item1)
        arrayList.add(item1)
        arrayList.add(item1)
        arrayList.add(item1)

        transactionAdapter.addItemList(arrayList)

        Log.d(">>>", "onCreate: "+transactionAdapter.itemCount)
        if (arrayList.size == 0){
            binding.llWrapSort.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }
    }

}