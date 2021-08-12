package com.mobile.bnkcl.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.DialogConnectionBinding
import com.mobile.bnkcl.databinding.DialogLanguageBinding
import com.mobile.bnkcl.databinding.DialogUpdateBinding

class LanguageDialog : BaseDialogFragment<DialogLanguageBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.dialog_language
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rbLa.setOnClickListener {
            LocaleHelper.setLocale(requireContext(), "en")
            Log.d(">>>", "onViewCreated: " + LocaleHelper.getLanguage(requireContext()))
        }
    }

}