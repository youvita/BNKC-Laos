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
import com.bnkc.library.data.type.AppLogin
import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.Constants.ANIMATE_NORMAL
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.databinding.TabItemViewBinding
import com.bnkc.sourcemodule.ui.TabViewPagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.databinding.ActivityMainBinding
import com.mobile.bnkcl.ui.cscenter.CSCenterActivity
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.dialog.LogOutDialog
import com.mobile.bnkcl.ui.home.HomeActivity
import com.mobile.bnkcl.ui.lease.service.LeaseServiceActivity
import com.mobile.bnkcl.ui.intro.IntroActivity
import com.mobile.bnkcl.ui.main.fragment.mypage.PageFragment
import com.mobile.bnkcl.ui.main.fragment.office.FindOfficeFragment
import com.mobile.bnkcl.ui.main.fragment.service.ServiceFragment
import com.mobile.bnkcl.ui.notice.NoticeActivity
import com.mobile.bnkcl.ui.otp.OtpActivity
import com.mobile.bnkcl.ui.pinview.PinCodeActivity
import com.mobile.bnkcl.ui.setting.SettingActivity
import com.mobile.bnkcl.ui.signup.TermsAndConditionsActivity
import com.mobile.bnkcl.ui.user.AccountInformationActivity
import com.mobile.bnkcl.utilities.UtilsGlide
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    private val viewModel: MainViewModel by viewModels()

    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager2? = null

    private var tabAdapter: TabViewPagerAdapter =
        TabViewPagerAdapter(supportFragmentManager, lifecycle)

    /**
     * menu title list
     */
    private val menuNames = arrayOf(
        R.string.tab_service,
        R.string.tab_my_page,
        R.string.tab_find_office
    )

    /**
     * menu icons list
     */
    private val menuIcons = arrayOf(
        R.drawable.selector_tab_loan_service,
        R.drawable.selector_tab_mypage,
        R.drawable.selector_tab_find_office
    )

    private var profileData: ProfileData? = null
    private val role: Int = 1
    private var lastIndex: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimateType(ANIMATE_NORMAL)
        setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        super.onCreate(savedInstanceState)

        viewModel.userRole = -1 //Custom : 0, Employee : 1 , Dealer : 2, Not yet login : -1
        viewModel.isLogin = sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()
        Log.d(">>>>", "Login ready ??? ${viewModel.isLogin}")

        initView()
        requestProfile()
        initBottomViewPager()
        initLiveData()

    }

    private fun requestProfile() {

        if (AppLogin.PIN.code != "N") {
            viewModel.getUserProfile()

            Glide.with(this)
                .load(R.drawable.rotate_loading_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.navMenu.ivLoading))

            val url = GlideUrl(
                sharedPrefer.getPrefer(Constants.KEY_START_URL).plus(Constants.IMAGE_URL),
                LazyHeaders.Builder()
                    .addHeader(
                        "Authorization",
                        "Bearer " + sharedPrefer.getPrefer(Constants.KEY_TOKEN)
                    )
                    .build()
            )
            UtilsGlide.loadCircle(
                this@MainActivity,
                url,
                binding.navMenu.ivProfile,
                binding.navMenu.ivLoading
            )
        }
    }

    private fun initLiveData() {

        viewModel.userProfileLiveData.observe(this) {
            profileData = it

            binding.navMenu.tvUserName.text = it.name
            binding.navMenu.tvUserId.text = it.accountNumber
            successListener()
        }

        viewModel.logoutLiveData.observe(this) {
            sharedPrefer.remove(Constants.KEY_TOKEN)
            sharedPrefer.remove(Constants.USER_ID)
            successListener()

            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        binding.navMenu.localeCode = Locale.getDefault().language

        if (viewModel.isLogin) {
            binding.navMenu.btnSignUp.visibility = View.GONE
            binding.navMenu.btnLogin.text = getString(R.string.nav_logout)
        }

        binding.navMenu.tvUserName.text = "User Unknown"
        binding.navMenu.tvUserId.text = ""

        binding.navMenu.llProfile.setOnClickListener(this)
        binding.navMenu.llHome.setOnClickListener(this)
        binding.navMenu.btnLogin.setOnClickListener(this)
        binding.navMenu.llNotice.setOnClickListener(this)
        binding.navMenu.btnPolicy.setOnClickListener(this)
        binding.navMenu.btnSignUp.setOnClickListener(this)
        binding.navMenu.llLanguage.setOnClickListener(this)
        binding.navMenu.llCsCenter.setOnClickListener(this)
        binding.navMenu.btnSetting.setOnClickListener(this)
        binding.navMenu.btnFacebook.setOnClickListener(this)
        binding.navMenu.btnCompanyProfile.setOnClickListener(this)
    }

    private fun initBottomViewPager() {
        try {

            viewPager = binding.viewPager
            tabLayout = binding.tabBottomMenu.mainTabLayout

            val findOfficeFragment = FindOfficeFragment()
            val pageFragment = PageFragment()
            tabAdapter.addFragment(ServiceFragment())
            tabAdapter.addFragment(PageFragment())
            tabAdapter.addFragment(findOfficeFragment)

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
                    lastIndex = position
                    when (position) {
                        1 -> {
                            if (AppLogin.PIN.code == "N") {
                                if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                                    val intent = Intent(this@MainActivity, OtpActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val loginIntent =
                                        Intent(this@MainActivity, PinCodeActivity::class.java)
                                    loginIntent.putExtra("pin_action", "login")
                                    loginIntent.putExtra(
                                        "from",
                                        LeaseServiceActivity::class.java.simpleName
                                    )
                                    loginIntent.putExtra(
                                        "username",
                                        sharedPrefer.getPrefer(Constants.USER_ID)
                                    )
                                    startActivity(loginIntent)
                                }
                            } else {
                                pageFragment.requestData()
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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val intent: Intent
            when (v.id) {
                R.id.ll_profile -> {

                    if (AppLogin.PIN.code == "N") {
                        if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
                            intent = Intent(this@MainActivity, OtpActivity::class.java)
                            startActivity(intent)
                        } else {
                            val loginIntent =
                                Intent(this@MainActivity, PinCodeActivity::class.java)
                            loginIntent.putExtra("pin_action", "login")
                            loginIntent.putExtra(
                                "from",
                                LeaseServiceActivity::class.java.simpleName
                            )
                            loginIntent.putExtra(
                                "username",
                                sharedPrefer.getPrefer(Constants.USER_ID)
                            )
                            startActivity(loginIntent)
                        }
                    } else {
                        startActivity(
                            Intent(
                                this,
                                AccountInformationActivity::class.java
                            ).putExtra("ACCOUNT_INFO", profileData)
                        )
                    }
                }
                R.id.ll_notice -> {
                    startActivity(Intent(this, NoticeActivity::class.java))
                }
                R.id.ll_cs_center -> {
                    if (AppLogin.PIN.code == "N"){
                        if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()){
                            startActivity(Intent(this, OtpActivity::class.java))

                        }else{
                            val intent = Intent(this, PinCodeActivity::class.java)
                            intent.putExtra("pin_action", "login")
                            intent.putExtra("from", MainActivity::class.java.simpleName)
                            intent.putExtra("username", sharedPrefer.getPrefer(Constants.USER_ID))
                            startActivity(intent)
                        }
                    }else{
                        startActivity(Intent(this, CSCenterActivity::class.java))
                    }

                }
                R.id.ll_home -> {
                    intent = Intent(this, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                R.id.ll_language -> {
                    val languageDialog = LanguageDialog()
                    languageDialog.show(
                        supportFragmentManager,
                        languageDialog.tag
                    )
                    languageDialog.onLangSelected {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                R.id.btn_facebook -> {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse("https://www.google.com")
                    startActivity(facebookIntent)
                }
                R.id.btn_company_profile -> {
                    intent = Intent(this, TermsAndConditionsActivity::class.java)
                    intent.putExtra(Constants.WEB_URL, Constants.WB_COM_PROFILE)
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.nav_company_profile))
                    startActivity(intent)
                }
                R.id.btn_policy -> {
                    intent = Intent(this, TermsAndConditionsActivity::class.java)
                    intent.putExtra(Constants.WEB_URL, Constants.WB_POLICY)
                    intent.putExtra(Constants.WEB_TITLE, getString(R.string.setting_03))
                    startActivity(intent)
                }
                R.id.btn_setting -> {
                    intent = Intent(this, SettingActivity::class.java)
                    if (sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()) {
                        intent.putExtra(
                            "push_alarm_enabled",
                            viewModel.userProfileLiveData.value!!.pushAlarmEnabled
                        )
                    }
                    startActivity(intent)
                }
                R.id.btn_sign_up -> {
                    val intent1 = Intent(this, OtpActivity::class.java)
                    intent1.putExtra("ACTION_TAG", "SIGN_UP")
                    startActivity(intent1)
                }
                R.id.btn_login -> {
                    if (viewModel.isLogin) {
                        val logOutDialog = LogOutDialog()
                        logOutDialog.onConfirmClickedListener {
                            viewModel.logout()
                            showLoading()
                        }
                        logOutDialog.show(
                            supportFragmentManager,
                            logOutDialog.tag
                        )
                    } else {
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

    override fun onResume() {
        super.onResume()
        requestProfile()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayout = null
        viewPager = null
    }

}