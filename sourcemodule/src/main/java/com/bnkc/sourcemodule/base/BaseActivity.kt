/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.sourcemodule.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.dialog.LoadingDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseActivity<T: ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: T

    private var loadingDialog: LoadingDialog? = null

    private var disposable: Disposable? = null

    private var systemDialog: SystemDialog? = null

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

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
    fun successListener() {
        disposable = RxJava.listen(RxEvent.ResponseSuccess::class.java).subscribe {
            if (loadingDialog != null) {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }
    }

    protected open fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                color == getColor(R.color.color_f5f7fc) -> {
                    // status bar white color -> text black
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = color
                }
                color != Color.WHITE -> {
                    window.statusBarColor = color
                }
                else -> {
                    // status bar white color -> text black
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = color
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when {
                color == resources.getColor(R.color.color_f5f7fc) -> {
                    // status bar white color -> text black
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = color
                }
                color != Color.WHITE -> {
                    window.statusBarColor = color
                }
                else -> {
                    window.statusBarColor = darkenColor(color)
                }
            }
        }
    }

    fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.8f
        return Color.HSVToColor(hsv)
    }

    protected open fun setStatusBarTransparent(
        activity: Activity,
        darkStatusBar: Boolean
    ): Boolean {
        val decorView = activity.window.decorView
        val isInMultiWindowMode =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInMultiWindowMode
        if (isInMultiWindowMode) {
            return false
        } else {
            var systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (darkStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            decorView.systemUiVisibility = systemUiVisibility
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window
                    .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.statusBarColor = Color.TRANSPARENT
            }
        }
        return true
    }


    /**
     * handle catch server error
     */
    private fun systemErrorListener() {
        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
            var title = it.title
            var message = it.message
            if (title == "" && message == "") {
                when (it.code) {
                    ErrorCode.UNKNOWN_ERROR -> {
                        title = getString(R.string.title_no_network)
                        message = getString(R.string.message_pls_check_network)
                    }
                    ErrorCode.TIMEOUT_ERROR -> {
                        // TODO: Request Timeout
                    }
                }
            }

            if (systemDialog == null) {
                systemDialog = SystemDialog.newInstance(title, message)
                systemDialog?.show(supportFragmentManager, systemDialog?.tag)
                systemDialog?.onConfirmClicked {
                    systemDialog = null
                }
            }
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