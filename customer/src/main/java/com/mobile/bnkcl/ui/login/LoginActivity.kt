package com.mobile.bnkcl.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLoginBinding
import com.mobile.bnkcl.ui.base.BaseActivity
import com.mobile.bnkcl.ui.language.LanguageDialog
import com.mobile.bnkcl.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = loginViewModel
        binding.localeCode = Locale.getDefault().language
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        loginRequest()

        chooseLanguage()

        loginViewModel.setData("ddd")
    }

    private fun chooseLanguage() {
        binding.ivLanguage.setOnClickListener {
            LanguageDialog().show(supportFragmentManager, null)
        }
    }

    /**
     * login request observable
     */
    private fun loginRequest() {
        loginViewModel.login.observe(this) {
            if (it) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * text watcher event changed listener
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {binding.enableLogin = loginViewModel.enableLogin()}
        override fun afterTextChanged(s: Editable?) {}
    }
}