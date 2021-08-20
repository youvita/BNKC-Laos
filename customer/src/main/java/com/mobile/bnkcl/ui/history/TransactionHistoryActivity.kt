package com.mobile.bnkcl.ui.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.lease.transaction.TransactionHistoryRequest
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryData
import com.mobile.bnkcl.databinding.ActivityTransactionHistoryBinding
import com.mobile.bnkcl.ui.adapter.TransactionHistoryAdapter
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionHistoryActivity : BaseActivity<ActivityTransactionHistoryBinding>(), View.OnClickListener {

    @Inject
    lateinit var transactionAdapter: TransactionHistoryAdapter

    private val viewModel: TransactionHistoryViewModel by viewModels()
    private var CONTRACT_NO: String? = null
    private lateinit var transactionHistoryRequest: TransactionHistoryRequest

    override fun getLayoutId(): Int {
        return R.layout.activity_transaction_history
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        transactionHistoryRequest = TransactionHistoryRequest()

        if (intent != null) {
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String
        }

        transactionHistoryRequest.contract_no = CONTRACT_NO
        transactionHistoryRequest.payment_date_dir = "asc"

        viewModel.getTotalLeaseSchedule(transactionHistoryRequest)
        viewModel.transactionHistoryLiveData.observe(this) {
            initAdapter(it.transactionHistory!!)
        }
    }

    private fun initToolbar() {
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
    }

    private fun initAdapter(transactionHistoryData: List<TransactionHistoryData>) {
        binding.transactionRecyclerview.adapter = transactionAdapter
        transactionAdapter.addItemList(transactionHistoryData)

        if (transactionHistoryData.isEmpty()) {
            binding.llWrapSort.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
        }
    }


}