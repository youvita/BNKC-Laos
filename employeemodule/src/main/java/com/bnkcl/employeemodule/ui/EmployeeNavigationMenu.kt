package com.bnkcl.employeemodule.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bnkc.sourcemodule.R
import com.google.android.material.navigation.NavigationView


class EmployeeNavigationMenu : NavigationView {

    var alreadyLogin = false

    constructor(context: Context?, isLogin : Boolean) : super(context!!) {
        alreadyLogin = isLogin
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {

        initView(context)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {

        initView(context)
    }

    private fun initView(context: Context?){

//        val inflater =
//            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val mBinding = DataBindingUtil.inflate(inflater,R.layout.nav_drawer_layout)

        val mBinding =LayoutInflater.from(context).inflate(R.layout.nav_drawer_layout, null, false)
        val btnFacebook = mBinding.findViewById<TextView>(R.id.btn_facebook)
        val btnCompanyProfile = mBinding.findViewById<TextView>(R.id.btn_company_profile)
        val btnPolicy = mBinding.findViewById<TextView>(R.id.btn_policy)
        val btnSetting = mBinding.findViewById<TextView>(R.id.btn_setting)
        val btnSignUP = mBinding.findViewById<TextView>(R.id.btn_sign_up)
        val tvLogin = mBinding.findViewById<TextView>(R.id.btn_login)

//        tvSignUp!!.text = "Dealer"
//        mBinding.btnSignup.visibility = View.GONE
//        val tvSignUp = mBinding.findViewById<TextView>(R.id.tv_signup)
//        tvSignUp.text = "Dealer"
//        tvSignUp.setTextColor(Color.parseColor("#262626"))
//        btnFacebook.setOnClickListener {
//            val facebookIntent = Intent(Intent.ACTION_VIEW)
//            facebookIntent.data = Uri.parse("https://www.google.com")
//            context!!.startActivity(facebookIntent)
//        }
//        btnCompanyProfile.setOnClickListener{
//
//        }
//        btnPolicy.setOnClickListener {
//            Toast.makeText(context, "Call from employee module SignUp :)", Toast.LENGTH_SHORT).show()
//        }
//        btnSetting.setOnClickListener {
//            Toast.makeText(context, "YCall from employee module Login :)", Toast.LENGTH_SHORT).show()
//        }
        btnSignUP.setOnClickListener {
            Toast.makeText(context, "Call from employee module SignUp :)", Toast.LENGTH_SHORT).show()
        }
        tvLogin.setOnClickListener {
            Toast.makeText(context, "YCall from employee module Login :)", Toast.LENGTH_SHORT).show()
        }
        if (alreadyLogin){
            btnSignUP.text = "Dealer"
            btnSignUP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dealer_ico, 0, 0, 0)
            btnSignUP.setTextColor(Color.parseColor("#262626"))
            tvLogin.text = "Logout"
            tvLogin.setOnClickListener {
                Toast.makeText(context, "You have logout success :)", Toast.LENGTH_SHORT).show()
            }

            btnSignUP.setOnClickListener {
                Toast.makeText(context, "You click dealer :)", Toast.LENGTH_SHORT).show()
            }
        }

        addView(mBinding)

//        try {
//            mainViewPager = binding.mainViewPager
//            mainTabLayout = binding.mainBottomMenu.mainTabLayout
////            if (intent != null) {
////                MainActivity.isSelectFromButton =
////                    intent.getBooleanExtra("IS_SELECT_FROM_BUTTON", false)
////            } else MainActivity.isSelectFromButton = false
////            profileResponse = GetMyProfileResponse()
//            mainViewPagerApapter = MainViewPagerApapter(supportFragmentManager, this)
//            val myPageFragment = PageFragment()
//            val loanServiceFragment = ServiceFragment()
//            val findOfficeFragment = FindOfficeFragment()
////            myPageFragment.setMyLoanCardClickedListener(this)
//            mainViewPagerApapter!!.addFragment(
//                myPageFragment,
//                getString(R.string.main_bottom_menu1),
//                R.drawable.selector_tab_myloan_on
//            )
//            mainViewPagerApapter!!.addFragment(
//                loanServiceFragment,
//                getString(R.string.main_bottom_menu2),
//                R.drawable.selector_tab_myloan_on
//            )
//            mainViewPagerApapter!!.addFragment(
//                findOfficeFragment,
//                getString(R.string.main_bottom_menu3),
//                R.drawable.selector_tab_find_office
//            )
//            mainViewPager.setAdapter(mainViewPagerApapter)
//            mainViewPager.setOffscreenPageLimit(3)
//            mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//                override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int
//                ) {
//                }
//
//                override fun onPageSelected(position: Int) {
//                    when (position) {
////                        0 -> myPageFragment.myLoanTabSelected()
//                        1 -> {
//                        }
//                        2 -> {
//                            findOfficeFragment.loanServiceTabSelected()
//                        }
//                    }
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {}
//            })
//            mainTabLayout.setupWithViewPager(mainViewPager)
//            for (i in 0 until mainTabLayout.getTabCount()) {
//                mainTabLayout.getTabAt(i)!!.customView = mainViewPagerApapter!!.getTabView(i)
//            }
////            pre_lang = PreferenceDelegator.getInstance(this).get(Constant.Language.LANGUAGE)
////            tvLang = findViewById(R.id.tv_user_name)
////            tvCustomerID = findViewById(R.id.tv_user_id)
////            ivImageProfile = findViewById(R.id.img_profile)
////            tvCustomerID.setText(
////                PreferenceDelegator.getInstance(this@MainActivity).get(Constant.LoginInfo.CUST_NO)
////            )
//            binding.btnNotification.setOnClickListener(this)
//            binding.btnOption.setOnClickListener(this)
//            findViewById(R.id.btn_setting).setOnClickListener(this)
//            findViewById(R.id.btn_language).setOnClickListener(this)
//            findViewById(R.id.btn_cs_center).setOnClickListener(this)
//            findViewById(R.id.btn_company_profile).setOnClickListener(this)
//            findViewById(R.id.btn_facebook).setOnClickListener(this)
//            findViewById(R.id.btn_notice).setOnClickListener(this)
//            val tvLang: TextView = findViewById(R.id.tv_lang)
//            tvLang.setText(R.string.language_03)
//            if (LocaleHelper.getLanguage(this@MainActivity)
//                    .equalsIgnoreCase("en")
//            ) tvLang.setCompoundDrawablesWithIntrinsicBounds(
//                0,
//                0,
//                R.drawable.en_flag,
//                0
//            ) else tvLang.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.kh_flag, 0)
//            initProfile()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

}