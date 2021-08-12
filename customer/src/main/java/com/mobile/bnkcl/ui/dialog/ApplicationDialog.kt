package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.DialogApplicationBinding
import com.mobile.bnkcl.databinding.DialogConnectionBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding
import com.mobile.bnkcl.ui.adapter.TransactionHistoryAdapter
import javax.inject.Inject

class ApplicationDialog : BaseDialogFragment<DialogApplicationBinding>() {
    @Inject
    lateinit var transactionAdapter: TransactionHistoryAdapter
    override fun getLayoutId(): Int {
        return R.layout.dialog_application
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

}