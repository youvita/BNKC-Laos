package com.mobile.bnkcl.ui.cscenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.ActionBar
import com.mobile.bnkcl.data.request.cscenter.ClaimDetailReq
import com.mobile.bnkcl.data.response.cscenter.ClaimDetailRes
import com.mobile.bnkcl.databinding.ActivityAskBNKCDetailBinding
import com.mobile.bnkcl.ui.cscenter.viewmodel.AskBNKCDetailViewModel
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AskBNKCDetailActivity : BaseActivity<ActivityAskBNKCDetailBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_ask_b_n_k_c_detail
    private val askBNKCDetailViewModel: AskBNKCDetailViewModel by viewModels()
    private var signUpDisposable: Disposable? = null

    private lateinit var claimDetailReq: ClaimDetailReq
    private lateinit var claimDetailRes: ClaimDetailRes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        claimDetailReq = ClaimDetailReq()
        claimDetailRes = ClaimDetailRes()


        checkError()
        getClaimDetailData()
        binding.ivBack.setOnClickListener(this)

    }

    private fun checkError(){
        //Session expired
        signUpDisposable = RxJava.listen(RxEvent.SessionExpired::class.java).subscribe{
            errorSessionDialog(it.title, it.message).onConfirmClicked {
                sharedPrefer.putPrefer(Constants.KEY_TOKEN, "")//clear token when session expired
                startActivity(Intent(this, PinCodeActivity::class.java))
            }
        }
    }
    private fun getClaimDetailData() {
        try {
            if (this != null) {
                claimDetailReq.claim_id = intent.getIntExtra("CLAIM_ID", 0)

                if (intent.hasExtra("CATEGORY")) {

                    claimDetailRes.category = intent.getStringExtra("CATEGORY")
                    if (claimDetailRes.category != null) {
                        when {
                            claimDetailRes.category.equals("3") -> askBNKCDetailViewModel.getClaimDetailData(claimDetailReq)
                            claimDetailRes.category.equals("1") ||
                                    claimDetailRes.category.equals("2") -> askBNKCDetailViewModel.getClaimDetailData(claimDetailReq)
                        }
                    }
                }
            }

            askBNKCDetailViewModel.claimDetailLiveData.observe(this) {

                Log.d("title", "getClaimDetailData: ${it.title}")
                binding.svScroll.visibility = View.VISIBLE
//                binding.llWrapContent.visibility = View.GONE
                binding.tvContent.visibility = View.VISIBLE

                binding.tvTitle.text = it.title
                binding.tvCreateOn.text = com.bnkc.sourcemodule.util.FormatUtils.Companion.getDateFormat(it.created_on, 6)
                binding.tvCategory.text = it.category
                binding.tvContent.text = it.content

                when (claimDetailRes.category) {
                    "1" -> binding.tvCategory.text = "Question" + " / "
                    "2" -> binding.tvCategory.text = "Comment" + " / "
                    "3" -> binding.tvCategory.text = "Loan" + " / "
                }

                if (null == it.replied_on || "".equals(it.replied_on, ignoreCase = true)) {
                    binding.tvAnswer.text = this.getString(R.string.cs_13)
                    binding.tvAnswer.background = ContextCompat.getDrawable(this,R.drawable.round_solid_ffeeee_18)
                    binding.tvAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_d7191f))

                } else {
                    binding.tvAnswer.text = this.getString(R.string.cs_12)
                    binding.tvAnswer.background = ContextCompat.getDrawable(this,R.drawable.round_solid_e0f2f1_18)
                    binding.tvAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_00695c))
                }


                if (null == it.reply || "".equals(it.reply, ignoreCase = true)) {
                    binding.llWrapReply.background = ContextCompat.getDrawable(this,R.drawable.round_solid_ffeeee_5)
                    binding.tvReply.text = getString(R.string.not_answer)
                    binding.tvTitleReply.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_not_answer_red_ico, 0, 0, 0)
                    binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.color_d7191f))
                    binding.tvTitleReply.setTextColor(ContextCompat.getColor(this,R.color.color_d7191f))
                } else {
                    binding.llWrapReply.background = getDrawable(R.drawable.round_solid_f8fcfc_5)
                    binding.tvReply.text = it.reply
                    binding.tvTitleReply.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_answer_green_ico, 0, 0, 0)
                    binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.color_00695c))
                    binding.tvTitleReply.setTextColor(ContextCompat.getColor(this,R.color.color_00695c))
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
            when (v?.id) {
                R.id.iv_back -> {
                    onBackPressed()
                }
            }

    }
}