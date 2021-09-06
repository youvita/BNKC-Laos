package com.mobile.bnkcl.ui.lease.calculate.result

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.util.FormatUtils
import com.bnkc.sourcemodule.util.Formats
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.lease.calculate.LeaseCalResponse
import com.mobile.bnkcl.databinding.ActivityCalculateResultBinding
import com.mobile.bnkcl.ui.lease.calculate.LeaseCalculateViewModel
import com.mobile.bnkcl.utilities.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculateResultActivity : BaseActivity<ActivityCalculateResultBinding>() {

    lateinit var leaseCalResponse: LeaseCalResponse
    val viewModel : LeaseCalculateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
        super.onCreate(savedInstanceState)

        if (intent != null){
            leaseCalResponse = intent.getSerializableExtra("leaseCalResponse") as LeaseCalResponse
        }
        binding.calculateViewModel = viewModel
        binding.model = leaseCalResponse
        initActionBar()
        initEvent()

        creatingTable()
    }

    private fun initActionBar() {
        val actionBar = binding.toolbar
        actionBar.setToolbarTitleWithActionBack(
            R.drawable.ic_nav_close_dark_btn,
            "Result"
        )
        actionBar.setOnMenuLeftClick { onBackPressed() }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_calculate_result
    }

    private fun initEvent(){
        binding.llDetail.setOnClickListener(View.OnClickListener {
            val matchParentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(binding.llDetail.width, View.MeasureSpec.EXACTLY)
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            binding.tbContainer.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            if (binding.tbContainer.visibility == View.VISIBLE) {
                UtilAnimation.collapse(binding.tbContainer, 300)
                binding.ivShowDetail.setImageResource(R.drawable.ic_unfold_ico)
            } else {
                UtilAnimation.expand(binding.tbContainer, 300)
                binding.ivShowDetail.setImageResource(R.drawable.ic_fold_ico)
            }
        })
    }

    private fun creatingTable() {
        val face: Typeface = Formats.getTypeFace(this, 1)!!
        val numberFace: Typeface = Formats.getTypeFace(this,  1)!!
        val numberFaceBold: Typeface = Formats.getTypeFace(this,  2)!!
//        val stk: TableLayout = findViewById(R.id.table_main)
        val tbrow0 = TableRow(this)
        val tv0 = TextView(this)
        tv0.text = getString(R.string.result_detail_01)
        tv0.setTextColor(ContextCompat.getColor(this,R.color.color_90A4AE))
        tv0.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tv0.textSize = 12f
        tv0.setPadding(0, UtilsSize.getValueInDP(this, 15), 0, 0)
        tv0.typeface = face
        tv0.gravity = Gravity.CENTER
        tbrow0.addView(tv0)
        val tv1 = TextView(this)
        tv1.text = getString(R.string.result_detail_02)
        tv1.setTextColor(ContextCompat.getColor(this, R.color.color_90A4AE))
        tv1.textSize = 12f
        tv1.typeface = face
        tv1.setPadding(0, UtilsSize.getValueInDP(this, 15), 0, 0)
        tv1.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tv1.gravity = Gravity.CENTER
        tbrow0.addView(tv1)
        val tv2 = TextView(this)
        tv2.text = getString(R.string.result_detail_03)
        tv2.setTextColor(ContextCompat.getColor(this,R.color.color_90A4AE))
        tv2.textSize = 12f
        tv2.gravity = Gravity.CENTER
        tv2.typeface = face
        tv2.setPadding(0, UtilsSize.getValueInDP(this, 15), 0, 0)
        tv2.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tbrow0.addView(tv2)
        val tv3 = TextView(this)
        tv3.text = getString(R.string.result_detail_04)
        tv3.setTextColor(ContextCompat.getColor(this,R.color.color_90A4AE))
        tv3.textSize = 12f
        tv3.typeface = face
        tv3.setPadding(0, UtilsSize.getValueInDP(this, 15), 0, 0)
        tv3.gravity = Gravity.CENTER
        tv3.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tbrow0.addView(tv3)
        val tv4 = TextView(this)
        tv4.text = getString(R.string.result_detail_05)
        tv4.setTextColor(ContextCompat.getColor(this,R.color.color_90A4AE))
        tv4.textSize = 12f
        tv4.setPadding(0, UtilsSize.getValueInDP(this, 15), 0, 0)
        tv4.typeface = face
        tv4.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tbrow0.addView(tv4)
        binding.tableMain.addView(tbrow0)
        for (i in 0 until leaseCalResponse.installments!!.size + 1) {

            if (i == leaseCalResponse.installments!!.size) {
                val tbfooter = TableRow(this)
                val lp = TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                //Loan amount
                val footer_1 = TextView(this)
                footer_1.text = getString(R.string.result_manage_001)
                footer_1.setTextColor(Color.parseColor("#050A19"))
                footer_1.gravity = Gravity.CENTER
                footer_1.typeface = Formats.getTypeFace(this, 2)
                footer_1.textSize = 12f
                tbfooter.addView(footer_1)

                //Principal
                val footer_2 = TextView(this)
                footer_2.text = FormatUtils.getNumberFormat(this, leaseCalResponse.total_principal_paid!!)
                footer_2.setTextColor(Color.parseColor("#050A19"))
                footer_2.gravity = Gravity.CENTER
                footer_2.typeface = numberFaceBold
                footer_2.textSize = 12f
                tbfooter.addView(footer_2)

                //Interest rate
                val footer_3 = TextView(this)
                footer_3.text = FormatUtils.getNumberFormat(this, leaseCalResponse.total_interest_paid!!)
                footer_3.setTextColor(Color.parseColor("#050A19"))
                footer_3.gravity = Gravity.CENTER
                footer_3.typeface = numberFaceBold
                footer_3.textSize = 12f
                tbfooter.addView(footer_3)

                //Repayment
                val footer_4 = TextView(this)
                footer_4.text = FormatUtils.getNumberFormat(this, leaseCalResponse.monthly_repayment!!)
                footer_4.setTextColor(Color.parseColor("#050A19"))
                footer_4.gravity = Gravity.CENTER
                footer_4.typeface = numberFaceBold
                footer_4.textSize = 12f
                tbfooter.addView(footer_4)

                //Balance
                val footer_5 = TextView(this)
                footer_5.text = "-"
                footer_5.setTextColor(Color.parseColor("#050A19"))
                footer_5.gravity = Gravity.CENTER
                footer_5.textSize = 12f
                footer_5.typeface = numberFaceBold
                tbfooter.addView(footer_5)
                tbfooter.setPadding(0, 25, 0, 10)
                tbfooter.layoutParams = lp
                binding.tableMain.addView(tbfooter)
            } else {
                val dataObj = leaseCalResponse.installments!![i]
                val tbrow = TableRow(this)
                val params = TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                //Loan amount
                val t1v = TextView(this)
                dataObj.seq?.let {
                    t1v.text = it.toString()
                }
                t1v.setTextColor(Color.parseColor("#050A19"))
                t1v.gravity = Gravity.CENTER
                t1v.textSize = 12f
                t1v.typeface = numberFace
                t1v.setPadding(0, 0, 10, 0)
                tbrow.addView(t1v)

                //Principal
                val t2v = TextView(this)
                t2v.text = FormatUtils.getNumberFormat(this, dataObj.principal.toString())
                t2v.setTextColor(Color.parseColor("#050A19"))
                t2v.gravity = Gravity.CENTER
                t2v.textSize = 12f
                t2v.typeface = numberFace
                t2v.setPadding(10, 0, 10, 0)
                tbrow.addView(t2v)

                //Interest rate
                val t3v = TextView(this)
                t3v.text = FormatUtils.getNumberFormat(this, dataObj.interest.toString())
                t3v.setTextColor(Color.parseColor("#050A19"))
                t3v.typeface = numberFace
                t3v.textSize = 12f
                t3v.setPadding(10, 0, 10, 0)
                t3v.gravity = Gravity.CENTER
                tbrow.addView(t3v)

                //Repayment
                val t4v = TextView(this)
                t4v.text = FormatUtils.getNumberFormat(this, dataObj.repayment.toString())
                t4v.setTextColor(Color.parseColor("#050A19"))
                t4v.typeface = numberFace
                t4v.textSize = 12f
                t4v.setPadding(10, 0, 10, 0)
                t4v.gravity = Gravity.CENTER
                tbrow.addView(t4v)

                //Balance
                val t5v = TextView(this)
                t5v.text = FormatUtils.getNumberFormat(this, dataObj.balance.toString())
                t5v.setTextColor(Color.parseColor("#050A19"))
                t5v.typeface = numberFace
                t5v.textSize = 12f
                t5v.gravity = Gravity.CENTER
                tbrow.addView(t5v)
                tbrow.setPadding(0, 25, 0, 0)
                tbrow.layoutParams = params
                binding.tableMain.addView(tbrow)
            }
        }
    }

}