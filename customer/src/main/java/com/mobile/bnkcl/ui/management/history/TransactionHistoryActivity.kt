package com.mobile.bnkcl.ui.management.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.lease.transaction.TransactionHistoryRequest
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryData
import com.mobile.bnkcl.databinding.ActivityTransactionHistoryBinding
import com.mobile.bnkcl.ui.adapter.TransactionHistoryAdapter
import com.mobile.bnkcl.ui.dialog.SortDialog
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionHistoryActivity : BaseActivity<ActivityTransactionHistoryBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var systemDialog: SystemDialog

    @Inject
    lateinit var transactionAdapter: TransactionHistoryAdapter
    private var sortCode: String? = "asc"
    private val viewModel: TransactionHistoryViewModel by viewModels()
    private var CONTRACT_NO: String? = null
    private var transactionHistoryRequest: TransactionHistoryRequest = TransactionHistoryRequest()


    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()
        initLiveData()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getTransactionHistory(transactionHistoryRequest)
            showLoading(true)
        }
    }

    private fun initLiveData() {

        viewModel.transactionHistoryLiveData.observe(this) {
            initAdapter(it.transactionHistory!!)
        }
    }

    override fun handleSessionExpired(icon: Int, title: String, message: String, button: String) {
        super.handleSessionExpired(icon, title, message, button)
        systemDialog = SystemDialog.newInstance(icon, title, message, button)
        systemDialog.show(supportFragmentManager, systemDialog.tag)
        systemDialog.onConfirmClicked {
            RunTimeDataStore.LoginToken.value = ""
            startActivity(Intent(this, PinCodeActivity::class.java))
        }
    }

    private fun initView() {

        if (intent != null) {
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO") as String
        }

        transactionHistoryRequest.contract_no = CONTRACT_NO
        transactionHistoryRequest.payment_date_dir = "asc"

    }

    private fun initToolbar() {
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
        binding.llSort.setOnClickListener(this)
    }

    private fun initAdapter(transactionHistoryData: List<TransactionHistoryData>) {
        binding.transactionRecyclerview.adapter = transactionAdapter
        transactionAdapter.clearItemList()
        transactionAdapter.addItemList(transactionHistoryData)

        if (transactionHistoryData.isEmpty()) {
            binding.llWrapSort.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_transaction_history
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
            R.id.ll_sort -> {
                val sortDialog = SortDialog(sortCode!!)
                sortDialog.show(supportFragmentManager, sortDialog.tag)
                sortDialog.onDismissListener {
                    if (sortDialog.sortCode != null) {
                        transactionHistoryRequest.payment_date_dir = sortDialog.sortCode
                        sortCode = sortDialog.sortCode

                        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                            viewModel.getTransactionHistory(transactionHistoryRequest)
                            showLoading(true)
                        }

                        if (sortDialog.sortCode.toString() != "desc") {
                            binding.tvSort.text = getString(R.string.history_oldest)
                        } else {
                            binding.tvSort.text = getString(R.string.history_newest)
                        }
                    }
                }
            }
        }
    }


}