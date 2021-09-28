package com.mobile.bnkcl.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
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

    private var profileData: ProfileData? = ProfileData()
    private var isUpdateProfile: Boolean = false
    private var isGetProfile: Boolean = false
    private val role: Int = 1
    private var lastIndex: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimateType(ANIMATE_NORMAL)
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_f5f7fc))
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

        Log.d(">>>", "USER_ID: " + sharedPrefer.getPrefer(Constants.USER_ID))
        Log.d(">>>", "AppLogin: " + AppLogin.PIN.code)

        if (sharedPrefer.getPrefer(Constants.USER_ID).isNullOrEmpty()) {
            binding.navMenu.ivProfile.setImageResource(R.drawable.ic_avatar_l)
            binding.navMenu.tvUserName.text = getString(R.string.nav_user_unknown)
            binding.navMenu.tvUserId.text = ""
        }

        if (AppLogin.PIN.code != "N" && !isGetProfile) {
            viewModel.getUserProfile()
            isGetProfile = true

            Glide.with(this)
                .load(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(DrawableImageViewTarget(binding.navMenu.ivLoading))

            binding.navMenu.ivProfile.setImageResource(0)
            UtilsGlide.loadCircle(
                this@MainActivity,
                binding.navMenu.ivProfile,
                binding.navMenu.ivLoading
            )
        } else if (AppLogin.PIN.code == "N") {

            binding.navMenu.tvUserName.text = if (sharedPrefer.getPrefer("name").isNullOrBlank()) getString(R.string.nav_user_unknown) else sharedPrefer.getPrefer("name")
            binding.navMenu.tvUserId.text = if (sharedPrefer.getPrefer("account_number").isNullOrEmpty()) "" else sharedPrefer.getPrefer("account_number")
            if (sharedPrefer.contain("name")) setUpLogOutBtn()

            if (!sharedPrefer.getPrefer(Constants.IMAGE_BITMAP).isNullOrEmpty()) {

                // decode image bitmap as bytes and set image as bitmap
                val imageAsBytes: ByteArray =
                    Base64.decode(sharedPrefer.getPrefer(Constants.IMAGE_BITMAP), Base64.DEFAULT)
                binding.navMenu.ivProfile.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        imageAsBytes,
                        0,
                        imageAsBytes.size
                    )
                )
            } else {
                binding.navMenu.ivProfile.setImageResource(R.drawable.ic_avatar_l)
            }

        }
    }

    private fun setUpLogOutBtn() {

        binding.navMenu.btnSignUp.visibility = View.GONE
        binding.navMenu.btnLogin.text = getString(R.string.nav_logout)
        binding.navMenu.btnLogin.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_logout_ico,
            0,
            0,
            0
        )
        binding.navMenu.vLine.visibility = View.GONE

    }

    private fun initLiveData() {

        viewModel.userProfileLiveData.observe(this) {

            setUpLogOutBtn()

            profileData = it
            binding.navMenu.tvUserName.text = it.name
            binding.navMenu.tvUserId.text = it.accountNumber

            sharedPrefer.putPrefer("name", it.name!!)
            sharedPrefer.putPrefer("account_number", it.accountNumber!!)
        }

        viewModel.logoutLiveData.observe(this) {
            RunTimeDataStore.LoginToken.value = ""
            sharedPrefer.remove(Constants.USER_ID)
            sharedPrefer.remove(Constants.IMAGE_BITMAP)
            sharedPrefer.remove("name")
            sharedPrefer.remove("account_number")

            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            AppLogin.PIN.code = "N"
        }
    }

    private fun initView() {
        binding.navMenu.localeCode = Locale.getDefault().language

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
            tabAdapter.addFragment(pageFragment)
            tabAdapter.addFragment(findOfficeFragment)

            viewPager?.adapter = tabAdapter
            viewPager?.isUserInputEnabled = false
            TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
                val binding = TabItemViewBinding.inflate(LayoutInflater.from(this))
                binding.menuName = menuNames[position]
                binding.menuIcon = menuIcons[position]
                tab.customView = binding.root
            }.attach()

            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {

                    when (tab?.position) {
                        0 -> {
                            setStatusBarColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.color_f5f7fc
                                )
                            )
                        }
                        1 -> {
                            setStatusBarColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.colorPrimaryDark
                                )
                            )

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
                            }
                        }
                        2 -> {
                            setStatusBarColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.colorPrimaryDark
                                )
                            )
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })


            binding.tabBottomMenu.menu.setOnClickListener {
                Log.d(">>>>", "Drawer opening")
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            var intent: Intent
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
                        intent = Intent(this, AccountInformationActivity::class.java)
                        intent.putExtra("ACCOUNT_INFO", profileData)
                        resultLauncher.launch(intent)
                    }
                }
                R.id.ll_notice -> {
                    startActivity(Intent(this, NoticeActivity::class.java))
                }
                R.id.ll_cs_center -> {
                    startActivity(Intent(this, CSCenterActivity::class.java))
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
                        finish()
                    }
                }
                R.id.btn_facebook -> {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse(Constants.WB_BNKCL_FB)
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
                        if (viewModel.userProfileLiveData.value != null) {
                            intent.putExtra(
                                "push_alarm_enabled",
                                viewModel.userProfileLiveData.value!!.pushAlarmEnabled
                            )
                        }
                    }
                    resultLauncher.launch(intent)
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

                            if (AppLogin.PIN.code == "N") {
                                RunTimeDataStore.LoginToken.value = ""
                                sharedPrefer.remove(Constants.USER_ID)
                                sharedPrefer.remove(Constants.IMAGE_BITMAP)
                                sharedPrefer.remove("name")
                                sharedPrefer.remove("account_number")

                                intent = Intent(this, HomeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                AppLogin.PIN.code = "N"
                            } else {
                                viewModel.logout()
                                showLoading(true)
                            }
                        }
                        logOutDialog.show(
                            supportFragmentManager,
                            logOutDialog.tag
                        )

                    } else {
                        intent = Intent(this, OtpActivity::class.java)
                        intent.putExtra("pin_action", "login")
                        startActivity(intent)
                    }
                }
            }
        }
    }

    /**
     * replace for deprecated onActivityResult
     */
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    isUpdateProfile = result.data!!.getBooleanExtra("IS_UPDATE_PROFILE", false)
                    if (isUpdateProfile) isGetProfile = false
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

    override fun onBackPressed() {
        if (viewPager?.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager?.currentItem = 0
        }
    }

}