package com.mobile.bnkcl.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RadioGroup
import com.bnkc.library.app.recreateLanguageChanged
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LanguageDialog : BaseDialogFragment<DialogLanguageBinding>(), RadioGroup.OnCheckedChangeListener {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

    private var langSelectedListener: (() -> Unit)? = null

    private lateinit var languageCode: String

    override fun getLayoutId(): Int {
        return R.layout.dialog_language
    }

    companion object {
        @SuppressLint("ConstantLocale")
        private val getLanguages: MutableList<String> = mutableListOf(
                "lo",
                Locale.ENGLISH.language
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageCode = LocaleHelper.getLanguage(requireContext())

        setCheckRadioButton()
    }

    fun onLangSelected(langSelectedListener: (() -> Unit)) =
        apply { this.langSelectedListener = langSelectedListener }

    private fun setCheckRadioButton() {
        when (languageCode) {
            Locale.ENGLISH.language -> binding.rbEn.isChecked = true
            else -> binding.rbLaos.isChecked = true
        }
        binding.rgLanguage.setOnCheckedChangeListener(this)
    }

    private fun changeLanguage(language: String) {
        Handler().postDelayed({
            LocaleHelper.setLanguage(requireContext(), language)
            recreateLanguageChanged()
        }, 100L)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId) {
            R.id.rb_laos -> {changeLanguage(getLanguages[0])}
            R.id.rb_en -> {changeLanguage(getLanguages[1])}
        }
    }
}