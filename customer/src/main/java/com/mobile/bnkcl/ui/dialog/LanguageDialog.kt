package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.view.View
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogLanguageBinding

class LanguageDialog : BaseDialogFragment<DialogLanguageBinding>(), View.OnClickListener{

    private var langSelectedListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_language
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rbLaos.setOnClickListener(this)
        binding.rbEn.setOnClickListener(this)
    }


    fun onLangSelected(langSelectedListener: (() -> Unit)) =
        apply { this.langSelectedListener = langSelectedListener }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rb_laos -> {
                LocaleHelper.setLocale(requireContext(), "lo")
                langSelectedListener?.invoke()
                dialog?.dismiss()
            }
            R.id.rb_en -> {
                LocaleHelper.setLocale(requireContext(), "en")
                langSelectedListener?.invoke()
                dialog?.dismiss()
            }
        }
    }
}