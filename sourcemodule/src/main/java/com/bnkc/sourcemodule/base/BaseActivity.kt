/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.sourcemodule.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.dialog.LoadingDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseActivity<T: ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: T

    private var loadingDialog: LoadingDialog? = null

    private var disposable: Disposable? = null

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

    @Inject
    lateinit var systemDialog: SystemDialog

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()

        successListener()

        systemErrorListener()
    }

    /**
     * init binding
     */
    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    /**
     * handle catch success
     */
    private fun successListener() {
        disposable = RxJava.listen(RxEvent.ResponseSuccess::class.java).subscribe {
            if (loadingDialog != null) {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }
    }

    /**
     * handle catch server error
     */
    private fun systemErrorListener() {
        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
            systemDialog = SystemDialog.newInstance(getString(it.title), getString(it.message as Int))
            systemDialog.show(supportFragmentManager, systemDialog.tag)
        }
    }

    /**
     * handle to show loading
     */
    fun showLoading() {
        loadingDialog = LoadingDialog()
        loadingDialog?.show(supportFragmentManager, loadingDialog?.tag)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        disposable = null
    }
}