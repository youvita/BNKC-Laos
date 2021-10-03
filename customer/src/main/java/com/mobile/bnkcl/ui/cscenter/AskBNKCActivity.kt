package com.mobile.bnkcl.ui.cscenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.AlertDialog
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityAskbnkcBinding
import com.mobile.bnkcl.ui.cscenter.viewmodel.AskBNKCViewModel
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class AskBNKCActivity : BaseActivity<ActivityAskbnkcBinding>(),View.OnClickListener {
    override fun getLayoutId(): Int = R.layout.activity_askbnkc
    private val askBNKCViewModel : AskBNKCViewModel by viewModels()
    private var signUpDisposable: Disposable? = null

//    @Inject
//    lateinit var systemDialog: SystemDialog
    private var subject: String = ""
    private var description : String = ""
    private lateinit var collapseToolBarLayout : CollapsingToolbarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))

        collapseToolBarLayout =binding.collToolbar

        initToolbar()
        initButton()
        observeData()

        /**
         * init hidden keyboard
         * */

        Utils.setHideKeyboard(this, binding.wrapContent)
    }

    private fun initToolbar(){
        collapseToolBarLayout.title = this.getString(R.string.cs_02)
        collapseToolBarLayout.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        collapseToolBarLayout.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)

    }
    private fun initButton(){
        binding.btnSubmit.setOnClickListener(this)
        binding.edtSubject.addTextChangedListener(inputText)
        binding.edtDescription.addTextChangedListener(inputText)

    }

    /**
     * catch error
     */
    private fun handleError() {
        askBNKCViewModel.handleError.observe(this) {
            val error = getErrorMessage(it)
            alertDialog = AlertDialog.newInstance(error.icon!!, error.code!!, error.message!!, error.button!!)
            alertDialog.show(supportFragmentManager, alertDialog.tag)
            alertDialog.onConfirmClicked {
                // session expired
                if (error.code == ErrorCode.UNAUTHORIZED) {
                    RunTimeDataStore.LoginToken.value = ""
                    startActivity(Intent(this, PinCodeActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun onRequestToSubmit(){
        askBNKCViewModel.getClaim("1", subject, description)

    }
    private fun observeData(){
        askBNKCViewModel.claimLiveData.observe(this){

            if (subject.isNotEmpty() && description.isNotEmpty()){
                askResult(true)
            }
        }

        handleError()
    }
    private fun askResult(result: Boolean) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("from", AskBNKCActivity::class.java.simpleName)
        intent.putExtra("result", result)
        results.launch(intent)
    }

    private var inputText = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            subject = binding.edtSubject.text.toString()
            description = binding.edtDescription.text.toString()
            binding.btnSubmit.isEnable(subject, description)
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.toolbar_left_button -> {
                onBackPressed()
                finish()
            }

            R.id.btn_submit -> {
                onRequestToSubmit()
            }
        }
    }


    var results = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            setResult(RESULT_OK)
            finish()
    }
    }
}