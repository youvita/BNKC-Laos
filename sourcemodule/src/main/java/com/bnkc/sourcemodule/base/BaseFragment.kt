
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
import com.bnkc.library.data.type.Loading
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.dialog.LoadingDialog
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseFragment<T: ViewDataBinding> : Fragment() {

    lateinit var binding: T

    private var loadingDialog: LoadingDialog? = null

    private var introDisposable: Disposable? = null

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        successListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return performDataBinding().root
    }

    private fun performDataBinding() : T{
        binding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding
    }

    /**
     * handle to dismiss loading
     */
    private fun dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    fun successListener() {
        introDisposable = RxJava.listen(RxEvent.ResponseSuccess::class.java).subscribe {
            if (Loading.Allow.dismiss) {
                dismissLoading()
            }
        }
    }

    fun showLoading(isLoading: Boolean) {
        Loading.Allow.dismiss = isLoading
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(childFragmentManager, loadingDialog?.tag)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        introDisposable?.dispose()
        introDisposable = null
    }
}