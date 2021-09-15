package com.mobile.bnkcl.ui.management.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
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
    lateinit var transactionAdapter: TransactionHistoryAdapter
    private var sortCode: String? = "asc"
    private val viewModel: TransactionHistoryViewModel by viewModels()
    private var CONTRACT_NO: String? = null
    private var transactionHistoryRequest: TransactionHistoryRequest = TransactionHistoryRequest()


    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()
        initDisposable()
        initLiveData()

        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getTransactionHistory(transactionHistoryRequest)
            showLoading()
        }
    }

    private fun initLiveData() {

        viewModel.transactionHistoryLiveData.observe(this) {
            successListener()
            initAdapter(it.transactionHistory!!)
        }
    }

    private fun initDisposable() {
        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }

//        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
//            errorDialog(it.code, it.title, it.message)
//        }
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
                            showLoading()
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