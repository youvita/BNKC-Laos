package com.mobile.bnkcl.ui.success

import android.os.Bundle
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.binding.BindingAdapters.enableButton
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityResultBinding


class ResultActivity : BaseActivity<ActivityResultBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_result

    private var tag = ""
    private var resultStatus = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (intent != null) {
            tag = intent.getStringExtra("ACTION_TAG")!!
            resultStatus = intent.getBooleanExtra("result", false)

        }
        assert(tag != null)

        when(tag){
            "ask_bnkc" -> {
                binding.tvResultTitle.text = if (resultStatus) getString(R.string.cs_14) else getString(R.string.cs_18)
                binding.tvResultDescription.text = if (resultStatus) getString(R.string.cs_15) else getString(R.string.cs_19)
                when {
                    resultStatus -> {
                        binding.vbResult.setNormalButton(R.drawable.round_solid_d7191f_8, resources.getColor(R.color.color_ffffff))
                        binding.ivReg.setImageResource(R.drawable.ic_badge_success_b_char)
                        binding.vbResult.setLabelButton(this.getString(R.string.cs_16))
                    }
                    else -> {
                        binding.vbResult.setNormalButton(R.drawable.round_stroke_d7191f_solid_ffffff_8, resources.getColor(R.color.color_d7191f))
                        binding.ivReg.setImageResource(R.drawable.ic_badge_failed_b_char)
                        binding.vbResult.setLabelButton(this.getString(R.string.comm_try_again))
                    }
                }

            }
        }
    }
}