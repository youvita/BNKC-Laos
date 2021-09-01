package com.mobile.bnkcl.ui.cscenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.binding.BindingAdapters.enableButton
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityAskbnkcBinding
import com.mobile.bnkcl.ui.cscenter.viewmodel.AskBNKCViewModel
import com.mobile.bnkcl.ui.success.ResultActivity
import com.mobile.bnkcl.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskBNKCActivity : BaseActivity<ActivityAskbnkcBinding>(),View.OnClickListener {
    override fun getLayoutId(): Int = R.layout.activity_askbnkc
    private val askBNKCViewModel : AskBNKCViewModel by viewModels()

    private var subject: String = ""
    private var description : String = ""
    private lateinit var collapseToolBarLayout : CollapsingToolbarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collapseToolBarLayout =binding.collToolbar

        initToolbar()
        initButton()
        observeData()

    }
    private fun initToolbar(){
        collapseToolBarLayout.title = this.getString(R.string.cs_02)
        collapseToolBarLayout.setExpandedTitleTypeface(Utils.getTypeFace(this, 3))
        collapseToolBarLayout.setCollapsedTitleTypeface(Utils.getTypeFace(this, 3))
        binding.toolbarLeftButton.setOnClickListener(this)

    }
    private fun initButton(){
        binding.btnSubmit.setLabelButton(this.getString(R.string.comm_submit))
        binding.btnSubmit.setOnClickListener(this)

        binding.edtSubject.addTextChangedListener(inputText)
        binding.edtDescription.addTextChangedListener(inputText)

    }

    private fun onRequestToSubmit(){
        askBNKCViewModel.getClaim("1", subject, description)

    }
    private fun observeData(){
        askBNKCViewModel.claimLiveData.observe(this){

            if (subject.isEmpty() || description.isEmpty()){

            }else{
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("ACTION_TAG", "ask_bnkc")
                result.launch(intent)
            }

        }
    }

    private var inputText = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            subject = binding.edtSubject.text.toString()
            description = binding.edtDescription.text.toString()
            setEnableSubmitButton(subject, description)
        }
    }

    fun setEnableSubmitButton(subject: String, desc: String){
        if(subject.isEmpty() || desc.isEmpty()){
            binding.btnSubmit.enableButton(false)
        }
        else {
            binding.btnSubmit.enableButton(true)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.toolbar_left_button -> onBackPressed()

            R.id.btn_submit -> {
                onRequestToSubmit()
            }
        }
    }


    var result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){

            val data : Intent? = result.data
            if (data != null) {
                if (data.getIntExtra("tab_index", 0) != 0) {
                    val intent = intent
                    intent.putExtra("tab_index", data.getIntExtra("tab_index", 0))
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

    }
    }
}