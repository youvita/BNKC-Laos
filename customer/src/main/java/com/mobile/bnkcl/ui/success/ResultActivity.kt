package com.mobile.bnkcl.ui.success

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityResultBinding
import com.mobile.bnkcl.ui.cscenter.AskBNKCActivity
import com.mobile.bnkcl.ui.cscenter.CSCenterActivity
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_result

    val viewModel : ResultViewModel by viewModels()

    var resultStatus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.context = this
        binding.resultViewModel = viewModel

        if (intent != null) {
            viewModel.from = intent.getStringExtra("from")!!
            resultStatus = intent.getBooleanExtra("result", false)
            viewModel.resultLiveData = resultStatus
        }

        observeViewModel()

        when(viewModel.from){

            Constants.SET_UP_PIN -> {
                viewModel.showSomeView = false
            }

            ApplyLeaseActivity::class.java.simpleName -> {
                viewModel.showSomeView = false
            }

        }

        binding.vbResult.text = viewModel.resultLabelButton()

    }

    private fun observeViewModel(){
        viewModel.actionLiveData.observe(this, {
            when(it){
                AskBNKCActivity::class.java.simpleName -> {
                    if (resultStatus){

                    }else{

                    }
                }
                Constants.SET_UP_PIN -> {
                    if (resultStatus){

                    }else{

                    }
                }
                Constants.SIGN_UP_FAIL -> {
                    if (resultStatus){

                    }else{

                    }
                }
                ApplyLeaseActivity::class.java.simpleName -> {
                    if (resultStatus){

                    }else{

                    }
                }
            }
        })
    }

}