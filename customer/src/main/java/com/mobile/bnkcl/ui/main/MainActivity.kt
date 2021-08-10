package com.mobile.bnkcl.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityMainBinding
import com.mobile.bnkcl.ui.adapter.CommentAdapter
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

    private var commentDisposable: Disposable? = null

    private var mainViewPagerAdapter: MainViewPagerApapter? = null
    var mainViewPager: ViewPager? = null
    private var mainTabLayout: TabLayout? = null

    private val role: Int = 1

    @Inject
    lateinit var commentAdapter: CommentAdapter

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        getCommentList()

        commentDisposable = RxJava.listen(RxEvent.CommentSuccess::class.java).subscribe {
            Log.d(">>>>", "Result::: ${it.value}")
        }

        try {
            mainViewPager = binding.mainViewPager
            mainTabLayout = binding.mainBottomMenu.mainTabLayout
//            if (intent != null) {
//                MainActivity.isSelectFromButton =
//                    intent.getBooleanExtra("IS_SELECT_FROM_BUTTON", false)
//            } else MainActivity.isSelectFromButton = false
//            profileResponse = GetMyProfileResponse()
            mainViewPagerAdapter = MainViewPagerApapter(supportFragmentManager, this)
            val myPageFragment = PageFragment()
            val loanServiceFragment = ServiceFragment()
            val findOfficeFragment = FindOfficeFragment()
            val menuFragment = MenuFragment()
//            myPageFragment.setMyLoanCardClickedListener(this)
            mainViewPagerAdapter!!.addFragment(
                loanServiceFragment,
                "Service",
                R.drawable.selector_tab_loan_service
            )
            mainViewPagerAdapter!!.addFragment(
                myPageFragment,
                "My Page",
                R.drawable.selector_tab_mypage
            )
            mainViewPagerAdapter!!.addFragment(
                findOfficeFragment,
                "Find Office",
                R.drawable.selector_tab_find_office
            )
            mainViewPagerAdapter!!.addFragment(
                menuFragment,
                "Menu",
                R.drawable.selector_tab_menu
            )
            mainViewPager!!.adapter = mainViewPagerAdapter
            mainViewPager!!.offscreenPageLimit = 4
            mainViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    when (position) {
//                        0 -> myPageFragment.myLoanTabSelected()
                        1 -> {
                        }
                        2 -> {
                            findOfficeFragment.loanServiceTabSelected()
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
            mainTabLayout!!.setupWithViewPager(mainViewPager)
            for (i in 0 until mainTabLayout!!.getTabCount()) {
                mainTabLayout!!.getTabAt(i)!!.customView = mainViewPagerAdapter!!.getTabView(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        val navigationMenu = NavigationMenu(context = this)

//        when(role){
//            0 -> {
//                val navigationMenu = NavigationMenu(this)
//                binding.v.addView(navigationMenu)
//                Log.d(">>>>","You login as ::: Customer")
//            }
//            1 -> {
//                val employeeNavigationMenu = EmployeeNavigationMenu(this)
//                binding.v.addView(employeeNavigationMenu)
//                Log.d(">>>>","You login as ::: Employee")
//            }
//            else -> {
//                val employeeNavigationMenu = EmployeeNavigationMenu(this)
//                binding.v.addView(employeeNavigationMenu)
//                Log.d(">>>>","You login as ::: Dealer")
//            }
//        }

    }

//    /**
//     * get comments
//     */
//    private fun getCommentList() {
//        binding.rvComment.adapter = commentAdapter
//        viewModel.getComments()
//
//        viewModel.comments.observe(this) {
//            if (it.isNullOrEmpty()) return@observe
//            commentAdapter.addItemList(it)
//            viewModel.cancelRequests()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        commentDisposable?.dispose()
        commentDisposable = null
    }

}