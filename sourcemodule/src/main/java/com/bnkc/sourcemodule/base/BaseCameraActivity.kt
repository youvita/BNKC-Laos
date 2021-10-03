/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.sourcemodule.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.databinding.ViewDataBinding
import com.bnkc.library.permission.AppPermission
import com.bnkc.library.permission.AppPermissionsFactory
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.dialog.AlertDialog
import javax.inject.Inject

abstract class BaseCameraActivity<T: ViewDataBinding>: BaseActivity<T>() {

    @Inject
    lateinit var permissionsFactory: AppPermissionsFactory

//    @Inject
//    lateinit var systemDialog: SystemDialo?

    protected open val appPermission: AppPermission by lazy {
        permissionsFactory.getPermission(AppPermissionsFactory.PERMISSION_CAMERA)
    }

    protected open fun onPermissionGranted() {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        appPermission.permissionResult(grantResults, {
            if (requestCode == AppPermissionsFactory.PERMISSION_CAMERA)
                this@BaseCameraActivity.onPermissionGranted()
            },
            {
                alertDialog = AlertDialog.newInstance(R.drawable.ic_badge_error, getString(R.string.notice), getString(R.string.permission_denied), getString(R.string.confirm))
                alertDialog.show(supportFragmentManager, alertDialog.tag)
                alertDialog.onConfirmClicked {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))
                }
            })
    }

    /**
     * request ask permission
     */
    fun askPermission() {
        if (appPermission.isPermissionGranted(this)) {
            // TODO: start normal
            this@BaseCameraActivity.onPermissionGranted()
        } else {
            appPermission.requestPermission(this) { perStrings, resultPerCode ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(perStrings, resultPerCode)
                }
            }
        }
    }
}