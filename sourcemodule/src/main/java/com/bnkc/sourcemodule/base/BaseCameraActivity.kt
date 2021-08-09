/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.sourcemodule.base

import android.os.Build
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bnkc.library.permission.AppPermission
import com.bnkc.library.permission.AppPermissionsFactory
import javax.inject.Inject

abstract class BaseCameraActivity<T: ViewDataBinding>: BaseActivity<T>() {

    @Inject
    lateinit var permissionsFactory: AppPermissionsFactory

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
                //TODO: alert inform user
                Log.d(">>>>", "permission denied")
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