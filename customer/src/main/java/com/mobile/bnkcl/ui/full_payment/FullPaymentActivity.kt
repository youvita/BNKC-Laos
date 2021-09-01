package com.mobile.bnkcl.ui.full_payment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityFullPaymentBinding
import com.mobile.bnkcl.ui.mobile_payment.MobilePaymentActivity
import com.mobile.bnkcl.utilities.UtilAnimation
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FullPaymentActivity : BaseActivity<ActivityFullPaymentBinding>(), View.OnClickListener {

    private val viewModel: FullPaymentViewModel by viewModels()
    private var REPAYMENT_DATE: String? = null
    private var CONTRACT_NO: String? = null
    private var TOTAL_AMOUNT: String? = null
    private var CODE_REPAYMENT_PLAN_EARLY: String? = "C0370"
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var myCalendar = Calendar.getInstance()

    override fun getLayoutId(): Int {
        return R.layout.activity_full_payment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        addBankAccountInformationTable()
        setUpClickListener()

        if (intent != null) {
            val c = Calendar.getInstance().time
            REPAYMENT_DATE = simpleDateFormat.format(c)
            REPAYMENT_DATE = "2021-08-23"
//            REPAYMENT_DATE = intent.getStringExtra("REPAYMENT_DATE")
            CONTRACT_NO = intent.getStringExtra("CONTRACT_NO")

            binding.btnDate.text = REPAYMENT_DATE
            binding.mobilePayment.tvAccountNumber.text = CONTRACT_NO
            binding.mobilePayment.tvCid.text = sharedPrefer.getPrefer(Constants.USER_ID)

            viewModel.getFullPayment(CONTRACT_NO!!, REPAYMENT_DATE!!, "asc")
            showLoading()
        }

        checkDate()
        initLiveData()

    }

    private fun initLiveData() {
        viewModel.fullPaymentLiveData.observe(this) {
            successListener()
            binding.fullPaymentInfo.fullRepaymentInfo = it

            binding.fullPaymentInfo.tvFullPaymentAmount.text =
                com.bnkc.sourcemodule.util.FormatUtils.getNumberFormat(this, it.fullRepayment!!)
            TOTAL_AMOUNT = it.fullRepayment

            if (it.othersData?.isNotEmpty() == true) {
                binding.fullPaymentInfo.llWrapOther.removeAllViews()
                for (i in it.othersData.indices) {
                    val view = View.inflate(this, R.layout.item_full_repayment_other, null)
                    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                    val tvAmount = view.findViewById<TextView>(R.id.tv_amount)
                    tvTitle.text = it.othersData[i].title
                    tvAmount.text = com.bnkc.sourcemodule.util.FormatUtils.getNumberFormat(
                        this,
                        it.othersData[i].amount!!
                    )
                    binding.fullPaymentInfo.llWrapOther.addView(view)
                }
            }
        }
    }

    private fun setUpClickListener() {
        binding.fullPaymentInfo.llOtherInfo.setOnClickListener(this)
        binding.btnDate.setOnClickListener(this)
        binding.btnCheck.setOnClickListener(this)
    }

    private fun checkDate() {
        try {
            val dateToday = Date()
            val chosenDate = simpleDateFormat.parse(REPAYMENT_DATE!!)
            if (chosenDate!! < dateToday) { //when chosen date is 'today', activate 'payment ui'
                binding.mobilePayment.llPay.isEnabled = true
                binding.mobilePayment.llPay.setOnClickListener(this)
//                binding.mobilePayment.tvPay.setCompoundDrawablesWithIntrinsicBounds(
//                    0,
//                    0,
//                    R.drawable.ic_radio_btn_on,
//                    0
//                )
                binding.mobilePayment.tvPay.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_d7191f
                    )
                )
                binding.mobilePayment.llPay.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.selector_d7191f_ffffee, null)
            } else {
                binding.mobilePayment.llPay.isEnabled = false
                binding.mobilePayment.llPay.setOnClickListener(null)
//                binding.mobilePayment.tvPay.setCompoundDrawablesWithIntrinsicBounds(
//                    0,
//                    0,
//                    R.drawable.ic_radio_btn_off,
//                    0
//                )
                binding.mobilePayment.tvPay.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_B6C1C7
                    )
                )
                binding.mobilePayment.llPay.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.round_solid_e1e5ec_8, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
            R.id.ll_other_info -> {
                val isExpandLoanInfo =
                    binding.fullPaymentInfo.llOtherExpandInfo.visibility == View.VISIBLE
                binding.fullPaymentInfo.ivFold.setImageDrawable(
                    if (isExpandLoanInfo) {
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unfold_ico, null
                        )
                    } else {
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_fold_ico, null)
                    }
                )
                UtilAnimation.expandCollpaseAnimation(
                    binding.fullPaymentInfo.llOtherExpandInfo,
                    300,
                    isExpandLoanInfo
                )
            }
            R.id.btn_date -> {
                val str = REPAYMENT_DATE!!.trim { it <= ' ' }.split("-").toTypedArray()
                val year = str[0].toInt()
                val month = str[1].toInt()
                val day = str[2].toInt()
                val locale = resources.configuration.locale
                Locale.setDefault(locale)
                val date =
                    OnDateSetListener { _, y, m, d ->
                        myCalendar.set(Calendar.YEAR, y)
                        myCalendar.set(Calendar.MONTH, m)
                        myCalendar.set(Calendar.DAY_OF_MONTH, d)
                        REPAYMENT_DATE = simpleDateFormat.format(myCalendar.time)
                        binding.btnDate.text = REPAYMENT_DATE
                        checkDate()
                    }

                //FYI Date can pick only from today to Future
                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DialogThemeDate,
                    date,
                    year,
                    month - 1,
                    day
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
            R.id.btn_check -> {
                viewModel.getFullPayment(CONTRACT_NO!!, "2021-08-04", "asc")
                showLoading()
            }
            R.id.ll_pay -> {
                val intent = Intent(this, MobilePaymentActivity::class.java)
                intent.putExtra("CONTRACT_NO", CONTRACT_NO)
                intent.putExtra("PAYMENT_AMOUNT", TOTAL_AMOUNT)
                intent.putExtra("REPAYMENT_PLAN", CODE_REPAYMENT_PLAN_EARLY)
                startActivity(intent)
            }
        }
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
        bankName.text = resources.getString(R.string.full_payment_014)
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
        currency.text = resources.getString(R.string.full_payment_015)
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
        accNum.text = resources.getString(R.string.acc_info_09)
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
}