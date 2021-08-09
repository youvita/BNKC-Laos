/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.library.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class CameraPermission : AppPermission {

   override fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   ) {
      val permissions =
         arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
         )
      startAskPer.invoke(permissions, AppPermissionsFactory.PERMISSION_CAMERA)
   }

   override fun permissionResult(
      grantResults: IntArray,
      onPerGranted: () -> Unit,
      onPerDenied: () -> Unit
   ) {
      if (grantResults.isNotEmpty()) {
         val permissionsNotGranted = grantResults.filter { it != PackageManager.PERMISSION_GRANTED }
         if (permissionsNotGranted.isEmpty()) {
            onPerGranted.invoke()
         } else {
            onPerDenied.invoke()
         }
      }
   }

   override fun isPermissionGranted(context: Context): Boolean {
      val permissions =
         arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      val permissionsNotGranted =
         permissions.filter {
            ContextCompat.checkSelfPermission(
               context,
               it
            ) != PackageManager.PERMISSION_GRANTED
         }
      return permissionsNotGranted.isEmpty()
   }
}