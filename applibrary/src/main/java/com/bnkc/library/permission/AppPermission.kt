/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.library.permission

import android.content.Context

interface AppPermission {

   fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   )

   fun permissionResult(
      grantResults: IntArray,
      onPerGranted: () -> Unit,
      onPerDenied: () -> Unit
   )

   fun isPermissionGranted(context: Context): Boolean

}