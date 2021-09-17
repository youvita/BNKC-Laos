package com.mobile.bnkcl.ui.management.bill

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_NORMAL
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.FormatUtils
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityBillPaymentBinding
import com.mobile.bnkcl.ui.management.mobile_payment.MobilePaymentActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillPaymentActivity : BaseActivity<ActivityBillPaymentBinding>(), View.OnClickListener {

    private var CONTRACT_NO: String? = null
    private var TOTAL_AMOUNT: String? = null
    private var CODE_REPAYMENT_PLAN_REGULAR: String? = "C0312"

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        setAnimateType(ANIMATE_NORMAL)
        super.onCreate(savedInstanceState)

        initToolbar()
        initView()
        initDisposable()

    }

    private fun initDisposable() {

        disposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe {
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                RunTimeDataStore.LoginToken.value = ""
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }

    private fun initView() {

        if (intent != null) {
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO")
            TOTAL_AMOUNT = intent.getStringExtra("TOTAL_PAYMENT")
            binding.tvPaymentAmount.text = FormatUtils.getNumberFormat(this, TOTAL_AMOUNT!!)

            binding.mobilePayment.tvAccountNumber.text = CONTRACT_NO
        }

        binding.mobilePayment.tvCid.text = sharedPrefer.getPrefer(Constants.USER_ID)
        binding.mobilePayment.llPay.setOnClickListener(this)

        addBankAccountInformationTable()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bill_payment
    }

    private fun addBankAccountInformationTable() {
        binding.mobilePayment.tableLayout.removeAllViews()
        val bankInfo = arrayOf(
            arrayOf("ACLEDA Bank", getString(R.string.curr_kip), "001-02-157144-2-5"),
            arrayOf("ACLEDA Bank", getString(R.string.usd), "001-02-157144-1-5"),
            arrayOf("WING", getString(R.string.curr_kip), "01947372"),
            arrayOf("WING", getString(R.string.usd), "01751517"),
            arrayOf(
                "TrueMoney",
                getString(R.string.curr_kip).plus(" & ").plus(getString(R.string.usd)),
                "7077"
            )
        )

        // Header
        val bankName = TextView(this)
        bankName.layoutParams =
            TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        bankName.gravity = Gravity.START
        bankName.setPadding(0, 10, 0, 10)
        bankName.text = resources.getString(R.string.full_bank_name)
        bankName.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
        bankName.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
        bankName.textSize = 13f
        bankName.typeface = Utils.getTypeFace(this, 1)

        // Header
        val currency = TextView(this)
        currency.layoutParams =
            TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        currency.gravity = Gravity.START
        currency.setPadding(0, 10, 0, 10)
        currency.text = resources.getString(R.string.full_currency)
        currency.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
        currency.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
        currency.textSize = 13f
        currency.typeface = Utils.getTypeFace(this, 1)

        // Header
        val accNum = TextView(this)
        accNum.layoutParams = TableRow.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        accNum.gravity = Gravity.END
        accNum.setPadding(0, 10, 0, 10)
        accNum.text = resources.getString(R.string.mbl_acc_number)
        accNum.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
        accNum.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
        accNum.textSize = 13f
        accNum.typeface = Utils.getTypeFace(this, 1)

        val tableRowHeader = TableRow(this)
        val trParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        tableRowHeader.setPadding(0, 0, 0, 0)
        tableRowHeader.layoutParams = trParams
        tableRowHeader.addView(bankName)
        tableRowHeader.addView(currency)
        tableRowHeader.addView(accNum)
        binding.mobilePayment.tableLayout.addView(tableRowHeader)

        for (i in bankInfo.indices) {
            val tableRow = TableRow(this)
            val params = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.setPadding(0, 0, 0, 0)
            tableRow.layoutParams = params

            for (j in 0 until bankInfo.size - 2) {
                val textView = TextView(this)
                when (j) {
                    bankInfo[i].size - 1 -> {
                        textView.layoutParams =
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        textView.gravity = Gravity.END
                        textView.typeface = Utils.getTypeFace(this, "en", 2)
                    }
                    0 -> {
                        textView.layoutParams = TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1F
                        )
                        textView.gravity = Gravity.START
                        textView.typeface = Utils.getTypeFace(this, "en", 1)
                    }
                    1 -> {
                        textView.layoutParams = TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1F
                        )
                        textView.gravity = Gravity.START
                        textView.typeface = Utils.getTypeFace(this, "en", 1)
                    }
                    else -> {
                        textView.layoutParams =
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        textView.gravity = Gravity.START
                        textView.typeface = Utils.getTypeFace(this, 1)
                    }
                }
                textView.setPadding(0, 10, 0, 10)
                textView.text = bankInfo[i][j]
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
                textView.setTextColor(ContextCompat.getColor(this, R.color.color_050a19))
                textView.textSize = 13f
                tableRow.addView(textView)
            }
            binding.mobilePayment.tableLayout.addView(tableRow)
        }
    }

    private fun initToolbar() {
        binding.collToolbar.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.collToolbar.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_left_button -> {
                finish()
            }
            R.id.ll_pay -> {
                val intent = Intent(this, MobilePaymentActivity::class.java)
                intent.putExtra("CONTRACT_NO", CONTRACT_NO)
                intent.putExtra("PAYMENT_AMOUNT", TOTAL_AMOUNT)
                intent.putExtra("REPAYMENT_PLAN", CODE_REPAYMENT_PLAN_REGULAR)
                startActivity(intent)
            }
        }
    }
}