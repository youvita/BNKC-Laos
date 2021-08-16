package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageDialog : BaseDialogFragment<DialogLanguageBinding>(), View.OnClickListener {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

    private var langSelectedListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_language
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rbLaos.setOnClickListener(this)
        binding.rbEn.setOnClickListener(this)

        val preLang : String = sharedPrefer.getPrefer("pre_lang").toString()
        setSelectedLang(preLang)
    }

    private fun setSelectedLang(pre_lang : String) {
        if (pre_lang == "en") {
            binding.rbEn.isChecked = true
        } else {
            binding.rbLaos.isChecked = true
        }
    }

    fun onLangSelected(langSelectedListener: (() -> Unit)) =
        apply { this.langSelectedListener = langSelectedListener }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rb_laos -> {
                LocaleHelper.setLocale(requireContext(), "lo")
                sharedPrefer.putPrefer("pre_lang", "lo")
                langSelectedListener?.invoke()
                dialog?.dismiss()
            }
            R.id.rb_en -> {
                LocaleHelper.setLocale(requireContext(), "en")
                sharedPrefer.putPrefer("pre_lang", "en")
                langSelectedListener?.invoke()
                dialog?.dismiss()
            }
        }
    }
}