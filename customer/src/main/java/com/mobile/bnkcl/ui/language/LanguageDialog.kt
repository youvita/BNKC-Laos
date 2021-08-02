package com.mobile.bnkcl.ui.language

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RadioGroup
import com.bnkc.library.app.recreateLanguageChanged
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityLanguageBinding
import java.util.*

class LanguageDialog: BaseBottomSheetDialog<ActivityLanguageBinding>(), RadioGroup.OnCheckedChangeListener {

    private lateinit var languageCode: String

    override fun getLayoutId(): Int {
        return R.layout.activity_language
    }

    companion object {
        private val getLanguages: MutableList<String> = mutableListOf(
            Locale.ENGLISH.language,
            Locale.SIMPLIFIED_CHINESE.language
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageCode = LocaleHelper.getLanguage(requireContext())
        setCheckRadioButton()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId) {
            R.id.rb_english -> {changeLanguage(getLanguages[0])}
            R.id.rb_chinese -> {changeLanguage(getLanguages[1])}
        }
    }

    override fun onDestroyView() {
        binding.rgLanguage.setOnCheckedChangeListener(null)
        super.onDestroyView()
    }

    private fun changeLanguage(language: String) {
        Handler().postDelayed({
            LocaleHelper.setLanguage(requireContext(), language)
            recreateLanguageChanged()
        }, 100L)
    }

    private fun setCheckRadioButton() {
        when (languageCode) {
            Locale.ENGLISH.language -> binding.rbEnglish.isChecked = true
            Locale.CHINESE.language -> binding.rbChinese.isChecked = true
        }
        binding.rgLanguage.setOnCheckedChangeListener(this)
    }

    /** start up made full screen */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }
}