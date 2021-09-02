package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.sourcemodule.base.BaseFullDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationData
import com.mobile.bnkcl.databinding.DialogApplicationBinding
import com.mobile.bnkcl.ui.adapter.LeaseRequestProcessAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationDialog(private val leaseApplicationList: List<LeaseApplicationData>, val type: Int) :
    BaseFullDialogFragment<DialogApplicationBinding>() {

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

        binding.transactionRecyclerview.adapter = leaseRequestProcessAdapter
        leaseRequestProcessAdapter.setLeaseProcessType(type)
        binding.transactionRecyclerview.removeItemDecoration(cardOffsetDecoration)
        binding.transactionRecyclerview.addItemDecoration(cardOffsetDecoration)
        cardRecyclerView.attachToRecyclerView(binding.transactionRecyclerview)
        leaseRequestProcessAdapter.clearItemList()
        leaseRequestProcessAdapter.addItemList(leaseApplicationList)
    }

}