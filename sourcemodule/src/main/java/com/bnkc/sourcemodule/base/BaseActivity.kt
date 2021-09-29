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
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.Loading
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.app.Constants.ANIMATE_NORMAL
import com.bnkc.sourcemodule.dialog.LoadingDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.bnkc.sourcemodule.util.UtilActivity
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefer: CredentialSharedPrefer

    lateinit var binding: T

    private var loadingDialog: LoadingDialog? = null

    private var disposable: Disposable? = null

    private var systemDialog: SystemDialog? = null

    private var animateType = 0

    @IntDef(value = [ANIMATE_LEFT, ANIMATE_NORMAL])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class ANIMATION_DIRECTION

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun setAnimateType(@ANIMATION_DIRECTION animateType: Int) {
        this.animateType = animateType
    }

    /**
     * handle error override method
     */
    open fun handleError(icon: Int, title: String, message: String, button: String) {}

    open fun handleError(code : Any, smg : String) {}

    /**
     * handle session expired override method
     */
    open fun handleSessionExpired(icon: Int, title: String, message: String, button: String) {}

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!UtilActivity.isCreated()) {
            if (animateType == ANIMATE_LEFT) {
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
            } else {
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
            }
        }
        performDataBinding()

        successListener()

        errorDialog()
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
            Log.d(">>>>>", "successListener: ${Loading.Allow.dismiss}")
            if (Loading.Allow.dismiss) {
                dismissLoading()
            }
        }
    }

    /**
     * handle catch server error
     */
    private fun errorDialog() {
        disposable = RxJava.listen(RxEvent.ServerError::class.java).subscribe {
            var icon = R.drawable.ic_badge_error
            var title = it.title
            var message = it.message
            var button = getString(R.string.confirm)

            if (title == "" && message == "") {
                when (it.code) {
                    ErrorCode.UNKNOWN_ERROR -> {
                        icon = R.drawable.ic_badge_no_internet
                        title = getString(R.string.title_no_network)
                        message = getString(R.string.message_pls_check_network)
                        button = getString(R.string.try_again)
                    }
                    ErrorCode.TIMEOUT_ERROR -> {
                        title = getString(R.string.title_timeout)
                        message = getString(R.string.message_timeout)
                    }
                }
            } else {
                when(it.title) {
                    ErrorCode.USER_EXISTS,
                    ErrorCode.USER_NOT_FOUND ,
                    ErrorCode.BAD_CREDENTIALS,
                    ErrorCode.INCORRECT_PASSWORD-> {
                        handleError(title, message)
                        return@subscribe
                    }
                    ErrorCode.UNAUTHORIZED -> {
                        // handle catch session expired
                        handleSessionExpired(R.drawable.ic_badge_error, it.title, it.message, getString(R.string.confirm))
                        return@subscribe
                    }
                }
            }

            if (systemDialog == null) {
                systemDialog = SystemDialog.newInstance(icon, title, message, button)
                systemDialog?.show(supportFragmentManager, systemDialog?.tag)
                systemDialog?.onConfirmClicked {
                    systemDialog = null
                }
            }

            dismissLoading()
        }
    }

    /**
     * handle to show loading
     */
    fun showLoading(isLoading: Boolean) {
        Loading.Allow.dismiss = isLoading
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(supportFragmentManager, loadingDialog?.tag)
        }
    }

    /**
     * handle to dismiss loading
     */
    fun dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        disposable = null

        loadingDialog = null
        systemDialog = null
    }

    override fun finish() {
        super.finish()
        if (!UtilActivity.isCreated()) {
            if (animateType == ANIMATE_LEFT) {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            } else {
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
            }
        }
        UtilActivity.isCreated(false)
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
}