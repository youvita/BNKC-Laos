package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bnkc.library.app.recreateLanguageChanged
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.FragmentMenuBinding
import com.mobile.bnkcl.ui.cscenter.CSCenterActivity
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.notice.NoticeActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.setting.SettingActivity
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import com.mobile.bnkcl.ui.user.AccountInformationActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(), View.OnClickListener {

    private val viewModel: MenuViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.context = requireContext()
        binding.menuViewModel = viewModel

        setClickListeners()

    }

    private fun setClickListeners() {
        binding.llProfile.setOnClickListener(this)
        binding.llNotice.setOnClickListener(this)
        binding.llCsCenter.setOnClickListener(this)
        binding.llHome.setOnClickListener(this)
        binding.llLanguage.setOnClickListener(this)
        binding.llFacebook.setOnClickListener(this)
        binding.llCompanyProfile.setOnClickListener(this)
        binding.llPolicy.setOnClickListener(this)
        binding.llSettings.setOnClickListener(this)
        binding.llSignup.setOnClickListener(this)
        binding.llLogin.setOnClickListener(this)
        binding.llLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id) {
                R.id.ll_profile -> {
                    startActivity(Intent(requireContext(), AccountInformationActivity::class.java))
                }
                R.id.ll_notice -> {
                    startActivity(Intent(requireContext(), NoticeActivity::class.java))
                }
                R.id.ll_cs_center -> {
                    startActivity(Intent(requireContext(), CSCenterActivity::class.java))
                }
                R.id.ll_home -> {

                }
                R.id.ll_language -> {
                    val languageDialog = LanguageDialog()
                    languageDialog.show(requireActivity().supportFragmentManager, languageDialog.tag)
                    languageDialog.onLangSelected {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        recreateLanguageChanged()
                    }
                }
                R.id.ll_facebook -> {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse("https://www.google.com")
                    startActivity(facebookIntent)
                }
                R.id.ll_company_profile -> {
                    startActivity(Intent(requireContext(), TermsAndConditionsActivity::class.java))
                }
                R.id.ll_policy -> {
                    startActivity(Intent(requireContext(), TermsAndConditionsActivity::class.java))
                }
                R.id.ll_settings -> {
                    startActivity(Intent(requireContext(), SettingActivity::class.java))
                }
                R.id.ll_signup -> {
                    startActivity(Intent(requireContext(), OtpActivity::class.java))
                }
                R.id.ll_login -> {
                    startActivity(Intent(requireContext(), OtpActivity::class.java))
                }
                R.id.ll_logout -> {
                    val logOutDialog = LogOutDialog()
                    logOutDialog.show(requireActivity().supportFragmentManager, logOutDialog.tag)
                }
            }
        }
    }

}