package com.mobile.bnkcl.ui.success

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityResultBinding
import com.mobile.bnkcl.ui.cscenter.AskBNKCActivity
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseActivity
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_result

    val viewModel : ResultViewModel by viewModels()
    var username : String? = null
    private var resultStatus = true

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
                viewModel.showSomeView = true
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
                        val intent = Intent(this, MainActivity::class.java)
                        setResult(RESULT_OK)
                        startActivity(intent)
                        finish()
                    }
                }
                Constants.RESET_PIN -> {
                    if (resultStatus){
                        val intent = Intent(this, PinCodeActivity::class.java)
                        intent.putExtra("pin_action", "login")
                        intent.putExtra("username", sharedPrefer.getPrefer(Constants.USER_ID))
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
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        finish()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        when (viewModel.from){
            Constants.RESET_PIN -> {
                val intent = Intent(this, SettingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                finish()
            }
            else -> {
                finish()
            }
        }
    }

}