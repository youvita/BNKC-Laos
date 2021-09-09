package com.mobile.bnkcl.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.databinding.TabItemViewBinding
import com.bnkc.sourcemodule.ui.TabViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityMainBinding
import com.mobile.bnkcl.ui.cscenter.CSCenterActivity
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.main.fragment.mypage.PageFragment
import com.mobile.bnkcl.ui.main.fragment.office.FindOfficeFragment
import com.mobile.bnkcl.ui.main.fragment.service.ServiceFragment
import com.mobile.bnkcl.ui.notice.NoticeActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.setting.SettingActivity
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    private val viewModel: MainViewModel by viewModels()

    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager2? = null

    private var tabAdapter: TabViewPagerAdapter = TabViewPagerAdapter(supportFragmentManager, lifecycle)

    /**
     * menu title list
     */
    private val menuNames = arrayOf(
        R.string.tab_service,
        R.string.tab_my_page,
        R.string.tab_find_office)


    /**
     * menu icons list
     */
     private val menuIcons = arrayOf(
        R.drawable.selector_tab_loan_service,
        R.drawable.selector_tab_mypage,
        R.drawable.selector_tab_find_office)

    private val role: Int = 1
    private var lastIndex: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        super.onCreate(savedInstanceState)

        viewModel.userRole = -1 //Custom : 0, Employee : 1 , Dealer : 2, Not yet login : -1
        viewModel.isLogin = sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()
        Log.d(">>>>", "Login ready ??? ${viewModel.isLogin}")
        try {

            viewPager = binding.viewPager
            tabLayout = binding.tabBottomMenu.mainTabLayout

            val findOfficeFragment = FindOfficeFragment()
            tabAdapter.addFragment(ServiceFragment())
            tabAdapter.addFragment(PageFragment())
            tabAdapter.addFragment(findOfficeFragment)
//            tabAdapter.addFragment(MenuFragment())

            viewPager?.adapter = tabAdapter
            viewPager?.isUserInputEnabled = false
            TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
                val binding = TabItemViewBinding.inflate(LayoutInflater.from(this))
                binding.menuName = menuNames[position]
                binding.menuIcon = menuIcons[position]
                tab.customView = binding.root
            }.attach()

            viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0)
                    else Log.d(">>>>", "onPageSelected: selected $position")
                    lastIndex = position
                    when (position) {
                        2 -> {
//                            findOfficeFragment.loanServiceTabSelected()
                            Log.d(">>>>", "onPageSelected: Menu opening")
                        }

                        1 -> {
                            if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                                val intent = Intent(this@MainActivity, OtpActivity::class.java)
                                intent.putExtra("ACTION_TAG", "REQUIRE_LOGIN")
                                intent.putExtra("LAST_INDEX", lastIndex)
                                startActivity(intent)
                            }
                        }
                    }

                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })

            binding.tabBottomMenu.menu.setOnClickListener {
                Log.d(">>>>", "Drawer opening")
                binding.drawerLayout.openDrawer(Gravity.RIGHT)
            }

            setClickListeners()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setClickListeners() {
        binding.navMenu.llProfile.setOnClickListener(this)
        binding.navMenu.llNotice.setOnClickListener(this)
        binding.navMenu.llCsCenter.setOnClickListener(this)
        binding.navMenu.llHome.setOnClickListener(this)
        binding.navMenu.llLanguage.setOnClickListener(this)
        binding.navMenu.btnFacebook.setOnClickListener(this)
        binding.navMenu.btnCompanyProfile.setOnClickListener(this)
        binding.navMenu.btnPolicy.setOnClickListener(this)
        binding.navMenu.btnSetting.setOnClickListener(this)
        binding.navMenu.btnSignUp.setOnClickListener(this)
        binding.navMenu.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val intent: Intent
            when(v.id) {
                R.id.ll_profile -> {
//                    startActivity(Intent(this, AccountInformationActivity::class.java).putExtra("ACCOUNT_INFO", profileData))
                }
                R.id.ll_notice -> {
                    startActivity(Intent(this, NoticeActivity::class.java))
                }
                R.id.ll_cs_center -> {
                    startActivity(Intent(this, CSCenterActivity::class.java))
                }
                R.id.ll_home -> {

                }
                R.id.ll_language -> {
                    val languageDialog = LanguageDialog()
                    languageDialog.show(supportFragmentManager, languageDialog.tag)
                    languageDialog.onLangSelected {
                        startActivity(Intent(this, MainActivity::class.java))
//                        recreateLanguageChanged()
                    }
                }
                R.id.btn_facebook -> {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse("https://www.google.com")
                    startActivity(facebookIntent)
                }
                R.id.btn_company_profile -> {
                    intent = Intent(this, TermsAndConditionsActivity::class.java)
                    intent.putExtra(Constants.WEB_URL, "/pages/company_profile.html")
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.nav_company_profile))
                    startActivity(intent)
                }
                R.id.btn_policy -> {
                    intent = Intent(this, TermsAndConditionsActivity::class.java)
                    intent.putExtra(Constants.WEB_URL, "/pages/policy.html")
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.setting_03))
                    startActivity(intent)
                }
                R.id.btn_setting -> {
                    intent = Intent(this, SettingActivity::class.java)
                    if (sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()) {
                        intent.putExtra(
                            "push_alarm_enabled",
                            viewModel.userProfileLiveData.value!!.push_alarm_enabled
                        )
                    }
                    startActivity(intent)
                }
                R.id.btn_sign_up -> {
//                    viewModel.goToSignUp()
                    val intent1 = Intent(this, OtpActivity::class.java)
                    intent1.putExtra("ACTION_TAG", "SIGN_UP")
                    startActivity(intent1)
                }
                R.id.btn_login -> {
                    if (viewModel.isLogin){
                        val logOutDialog = LogOutDialog()
                        logOutDialog.onConfirmClickedListener {
                            sharedPrefer.remove(Constants.USER_ID)
                            Log.d(">>>>", "Remove ready ??? ${sharedPrefer.getPrefer(Constants.USER_ID)}")
                            startActivity(Intent(this, OtpActivity::class.java))
                        }
                        logOutDialog.show(this.supportFragmentManager, logOutDialog.tag)
                    }else{
                        startActivity(Intent(this, OtpActivity::class.java))
                    }
                }
                R.id.btn_logout -> {
                    val logOutDialog = LogOutDialog()
                    logOutDialog.show(this.supportFragmentManager, logOutDialog.tag)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayout = null
        viewPager = null
    }

}