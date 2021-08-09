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

class StoragePermission : AppPermission {

   override fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   ) {
      startAskPer.invoke(
         arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
              AppPermissionsFactory.PERMISSION_STORAGE
      )
   }

   override fun permissionResult(
      grantResults: IntArray,
      onPerGranted: () -> Unit,
      onPerDenied: () -> Unit
   ) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         onPerGranted.invoke()
      } else {
         onPerDenied.invoke()
      }
   }

   override fun isPermissionGranted(context: Context): Boolean {
      return ContextCompat.checkSelfPermission(
         context,
         Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
   }
}