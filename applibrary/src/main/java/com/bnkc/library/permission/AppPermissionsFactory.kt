/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.library.permission

class AppPermissionsFactory {

   fun getPermission(permissionCode: Int): AppPermission {
      return when (permissionCode) {
         PERMISSION_STORAGE -> StoragePermission()
         PERMISSION_CAMERA -> CameraPermission()
         else -> StoragePermission()
      }
   }

   companion object {
      const val PERMISSION_STORAGE  = 1
      const val PERMISSION_CAMERA   = 2
   }
}