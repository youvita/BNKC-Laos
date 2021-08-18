package com.mobile.bnkcl.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.databinding.TabItemViewBinding
import com.bnkc.sourcemodule.ui.TabViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityMainBinding
import com.mobile.bnkcl.ui.main.fragment.menu.MenuFragment
import com.mobile.bnkcl.ui.main.fragment.mypage.PageFragment
import com.mobile.bnkcl.ui.main.fragment.office.FindOfficeFragment
import com.mobile.bnkcl.ui.main.fragment.service.ServiceFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

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
        R.string.tab_find_office,
        R.string.tab_menu)


    /**
     * menu icons list
     */
     private val menuIcons = arrayOf(
        R.drawable.selector_tab_loan_service,
        R.drawable.selector_tab_mypage,
        R.drawable.selector_tab_find_office,
        R.drawable.selector_tab_menu)

    private val role: Int = 1

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        super.onCreate(savedInstanceState)

        viewModel.userRole = -1 //Custom : 0, Employee : 1 , Dealer : 2, Not yet login : -1
        viewModel.isLogin = sharedPrefer.getPrefer(Constants.USER_ID)!!.isNotEmpty()

        try {

            viewPager = binding.viewPager
            tabLayout = binding.tabBottomMenu.mainTabLayout

            tabAdapter.addFragment(ServiceFragment())
            tabAdapter.addFragment(PageFragment())
            tabAdapter.addFragment(FindOfficeFragment())
            tabAdapter.addFragment(MenuFragment())

            viewPager?.adapter = tabAdapter

            TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
                val binding = TabItemViewBinding.inflate(LayoutInflater.from(this))
                binding.menuName = menuNames[position]
                binding.menuIcon = menuIcons[position]
                tab.customView = binding.root
            }.attach()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayout = null
        viewPager = null
    }

}