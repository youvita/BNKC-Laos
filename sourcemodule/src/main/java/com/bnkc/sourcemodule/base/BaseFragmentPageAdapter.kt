
/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.sourcemodule.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.dialog.LoadingDialog
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseFragmentPageAdapter<T: ViewDataBinding>(fm: FragmentManager, private val context: Context) : FragmentStatePagerAdapter(fm) {

    lateinit var binding: T
//
    @LayoutRes
    abstract fun getLayoutId(): Int
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        successListener()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return performDataBinding().root
//    }
//
//    private fun performDataBinding() : T {
//    val view: View = LayoutInflater.from(context).inflate(R.layout.tab_item_view, null)
//        binding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
//        binding.lifecycleOwner = this
//        binding.executePendingBindings()
//        return binding
//    }
}