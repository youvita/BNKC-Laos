/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.09
 */
package com.bnkc.library.util

import android.content.Context

object AppUtil {

    /**
     * get app version
     */
    fun getVersionName(context: Context): String? {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

}