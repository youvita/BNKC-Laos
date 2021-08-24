package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.sourcemodule.base.BaseFullDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.lease.LeaseInfoData
import com.mobile.bnkcl.databinding.DialogApplicationBinding
import com.mobile.bnkcl.ui.adapter.LeaseRequestProcessAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationDialog : BaseFullDialogFragment<DialogApplicationBinding>() {

    @Inject
    lateinit var leaseRequestProcessAdapter: LeaseRequestProcessAdapter

    @Inject
    lateinit var cardOffsetDecoration: CardOffsetDecoration

    private var cardRecyclerView: CardRecyclerView = CardRecyclerView()


    override fun getLayoutId(): Int {
        return R.layout.dialog_application
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mutableListOf<LeaseInfoData>()
        val item = LeaseInfoData()

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