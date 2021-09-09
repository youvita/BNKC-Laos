package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.main.MainActivity
import com.mobile.bnkcl.ui.main.MainViewModel
import com.mobile.bnkcl.ui.notice.NoticeActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.setting.SettingActivity
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import com.mobile.bnkcl.ui.user.AccountInformationActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(), View.OnClickListener {

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
        binding.localeCode = Locale.getDefault().language

        initView()
        initLiveData()

        when (mainViewModel.userRole) {
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


    }

    private fun initView() {

        if (mainViewModel.isLogin) {
            binding.btnSignUp.visibility = View.GONE
            binding.btnLogin.text = getString(R.string.nav_logout)

        }

        binding.llProfile.setOnClickListener(
            if (sharedPrefer.getPrefer(Constants.KEY_TOKEN).isNullOrEmpty()) null else this
        )

        if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            binding.tvUserName.text = "User Unknown"
        }

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

    private fun initLiveData() {

        viewModel.userProfileLiveData.observe(requireActivity()) {
            profileData = it

            binding.tvUserName.text = it.name
            binding.tvUserId.text = it.account_number
        }

        viewModel.logoutLiveData.observe(requireActivity()) {
            sharedPrefer.remove(Constants.KEY_TOKEN)
            sharedPrefer.remove(Constants.USER_ID)

            val intent = Intent(requireActivity(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            successListener()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            viewModel.getUserProfile()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val intent: Intent
            when (v.id) {
                R.id.ll_profile -> {
                    startActivity(
                        Intent(
                            requireContext(),
                            AccountInformationActivity::class.java
                        ).putExtra("ACCOUNT_INFO", profileData)
                    )
                }
                R.id.ll_notice -> {
                    startActivity(Intent(requireContext(), NoticeActivity::class.java))
                }
                R.id.ll_cs_center -> {
                    startActivity(Intent(requireContext(), CSCenterActivity::class.java))
                }
                R.id.ll_home -> {
                    intent = Intent(requireActivity(), HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                R.id.ll_language -> {
                    val languageDialog = LanguageDialog()
                    languageDialog.show(
                        requireActivity().supportFragmentManager,
                        languageDialog.tag
                    )
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
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.nav_company_profile))
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
                    if (mainViewModel.isLogin) {
                        val logOutDialog = LogOutDialog()
                        logOutDialog.onConfirmClickedListener {
                            viewModel.logout()
                            showLoading()
                        }
                        logOutDialog.show(
                            requireActivity().supportFragmentManager,
                            logOutDialog.tag
                        )
                    } else {
                        startActivity(Intent(requireContext(), OtpActivity::class.java))
                    }
                }
                R.id.btn_logout -> {
                    val logOutDialog = LogOutDialog()
                    logOutDialog.show(requireActivity().supportFragmentManager, logOutDialog.tag)
                }
            }
        }
    }

}