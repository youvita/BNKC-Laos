package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.databinding.DialogApplicationBinding
import com.mobile.bnkcl.ui.adapter.LeaseRequestProcessAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationDialog : BaseDialogFragment<DialogApplicationBinding>() {

    @Inject
    lateinit var leaseRequestProcessAdapter: LeaseRequestProcessAdapter

    @Inject
    lateinit var cardOffsetDecoration: CardOffsetDecoration

    @Inject
    lateinit var cardRecyclerView: CardRecyclerView


    override fun getLayoutId(): Int {
        return R.layout.dialog_application
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mutableListOf<CommentsItem>()
        val item = CommentsItem()
        item.postId = 1
        item.id = 1
        item.name = "1"
        item.email = "1"
        item.body = "1"

        for (i in 0..5) {
            list.add(i, item)
        }

        binding.transactionRecyclerview.adapter = leaseRequestProcessAdapter
        leaseRequestProcessAdapter.setLeaseProcessType(4)
        binding.transactionRecyclerview.removeItemDecoration(cardOffsetDecoration)
        binding.transactionRecyclerview.addItemDecoration(cardOffsetDecoration)
        cardRecyclerView.attachToRecyclerView(binding.transactionRecyclerview)

        leaseRequestProcessAdapter.addItemList(list)
    }

}