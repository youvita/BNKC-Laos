package com.mobile.bnkcl.ui.success

import android.content.Intent
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
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_result

    val viewModel : ResultViewModel by viewModels()
    var username : String? = null
    var resultStatus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.context = this
        binding.resultViewModel = viewModel

        if (intent != null) {
            viewModel.from = intent.getStringExtra("from")!!
            username = intent.getStringExtra("username")
            resultStatus = intent.getBooleanExtra("result", false)
            viewModel.resultLiveData = resultStatus
        }

        observeViewModel()

        when(viewModel.from){

            Constants.RESET_PIN -> {
                viewModel.showSomeView = false
            }

            ApplyLeaseActivity::class.java.simpleName -> {
                viewModel.showSomeView = false
            }

            AskBNKCActivity::class.java.simpleName ->{
                viewModel.showSomeView = false
            }

            Constants.SIGN_UP -> {
                viewModel.showSomeView = true
            }

        }

        binding.vbResult.text = viewModel.resultLabelButton()

    }

    private fun observeViewModel(){
        viewModel.actionLiveData.observe(this, {
            when(it){
                AskBNKCActivity::class.java.simpleName -> {
                    if (resultStatus){
                        var intent = Intent(this,CSCenterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        var intent = Intent(this,AskBNKCActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                Constants.RESET_PIN -> {
                    if (resultStatus){
                        val intent = Intent(this, PinCodeActivity::class.java)
                        intent.putExtra("pin_action", "login")
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    }else{
                        finish()
                    }
                }
                Constants.SIGN_UP -> {
                    if (resultStatus){
                        val intent = Intent(this, PinCodeActivity::class.java)
                        intent.putExtra("pin_action", "login")
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    }else{
                        finish()
                    }
                }
                ApplyLeaseActivity::class.java.simpleName -> {
                    if (resultStatus){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        finish()
                    }
                }
            }
        })
    }

}