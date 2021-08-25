package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bnkc.library.app.recreateLanguageChanged
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.FragmentMenuBinding
import com.mobile.bnkcl.ui.cscenter.CSCenterActivity
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.main.MainViewModel
import com.mobile.bnkcl.ui.notice.NoticeActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.setting.SettingActivity
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import com.mobile.bnkcl.ui.user.AccountInformationActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() , View.OnClickListener{

    private val viewModel: MenuViewModel by viewModels()
    private var profileData: ProfileData? = null

    private var userRole = 0

//    var navigationMenu : NavigationMenu = NavigationMenu(context, false)

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.context = requireContext()
        binding.menuViewModel = viewModel
        profileData = ProfileData()

        viewModel.userProfileLiveData.observe(requireActivity()) {
            profileData = it
            Log.d(">>>", "onViewCreated: " + profileData!!.account_number)
        }

        if (mainViewModel.isLogin){
            binding.btnSignUp.visibility = View.GONE
            binding.btnLogin.text = "Logout"
//            binding.btnLogin.setOnClickListener {
//
//            }
        }

        when(mainViewModel.userRole){
            0 -> { //Customer
//                var navigationMenu = NavigationMenu(context, true)
//                binding.mainMenu.addView(navigationMenu)

            }
            1 -> { //Employee
//                var navigationMenu = EmployeeNavigationMenu(context, true)
//                navigationMenu.setOnClickListener {
//                    Log.d(">>>>>>>>", "menu click id ${it.id}")
//                    if (it.id == R.id.btn_facebook){
//                        val facebookIntent = Intent(Intent.ACTION_VIEW)
//                        facebookIntent.data = Uri.parse("https://www.google.com")
//                        startActivity(facebookIntent)
//                    }else if (it.id == R.id.btn_setting){
//                        startActivity(Intent(requireContext(), SettingActivity::class.java))
//                    }
//                }
//                binding.mainMenu.addView(navigationMenu)
            }
            2 -> { //Dealer
//                var navigationMenu = NavigationMenu(context, true)
//                binding.mainMenu.addView(navigationMenu)
            }
            else -> { //Not yet login
//                val navigationMenu = NavigationMenu(context, false)
//                navigationMenu.btnFacebook!!.setOnClickListener {
//                    val facebookIntent = Intent(Intent.ACTION_VIEW)
//                    facebookIntent.data = Uri.parse("https://www.google.com")
//                    startActivity(facebookIntent)
//                }
//                navigationMenu.btnSetting!!.setOnClickListener {
//                    startActivity(Intent(requireContext(), SettingActivity::class.java))
//                }
//                binding.mainMenu.addView(navigationMenu)
            }
        }

        setClickListeners()

    }

    override fun onResume() {
        super.onResume()
        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getUserProfile()
        }
    }

    private fun setClickListeners() {
        binding.llProfile.setOnClickListener(this)
        binding.llNotice.setOnClickListener(this)
        binding.llCsCenter.setOnClickListener(this)
        binding.llHome.setOnClickListener(this)
        binding.llLanguage.setOnClickListener(this)
        binding.btnFacebook.setOnClickListener(this)
        binding.btnCompanyProfile.setOnClickListener(this)
        binding.btnPolicy.setOnClickListener(this)
        binding.btnSetting.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val intent: Intent
            when(v.id) {
                R.id.ll_profile -> {
                    startActivity(Intent(requireContext(), AccountInformationActivity::class.java).putExtra("ACCOUNT_INFO", profileData))
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
                R.id.btn_facebook -> {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse("https://www.google.com")
                    startActivity(facebookIntent)
                }
                R.id.btn_company_profile -> {
                    intent = Intent(requireContext(), TermsAndConditionsActivity::class.java)
                    intent.putExtra(Constants.WEB_URL, "/pages/company_profile.html")
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.nav_content_006))
                    startActivity(intent)
                }
                R.id.btn_policy -> {
                    intent = Intent(requireContext(), TermsAndConditionsActivity::class.java)
                    intent.putExtra(Constants.WEB_URL, "/pages/policy.html")
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.setting_03))
                    startActivity(intent)
                }
                R.id.btn_setting -> {
                    intent = Intent(requireContext(), SettingActivity::class.java)
                    if (sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()) {
                        intent.putExtra(
                            "push_alarm_enabled",
                            viewModel.userProfileLiveData.value!!.push_alarm_enabled
                        )
                    }
                    startActivity(intent)
                }
                R.id.btn_sign_up -> {
                    viewModel.goToSignUp()
                }
                R.id.btn_login -> {
                    startActivity(Intent(requireContext(), OtpActivity::class.java))
                }
                R.id.btn_logout -> {
                    val logOutDialog = LogOutDialog()
                    logOutDialog.show(requireActivity().supportFragmentManager, logOutDialog.tag)
                }
            }
        }
    }

}