package com.bnkc.sourcemodule.util

import android.content.Context
import android.content.Intent

object ComUtil {

    fun setBadge(ctx: Context, count: Int) {
        val launcherClassName = getLauncherClassName(ctx)
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        intent.putExtra("badge_count", count)
        intent.putExtra("badge_count_package_name", ctx.packageName)
        intent.putExtra("badge_count_class_name", launcherClassName)
        ctx.sendBroadcast(intent)
    }

    private fun getLauncherClassName(ctx: Context): String {
        val pm = ctx.packageManager

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfos = pm.queryIntentActivities(intent, 0)
        for (resolveInfo in resolveInfos) {
            val pkgName = resolveInfo.activityInfo.applicationInfo.packageName
            if (pkgName.equals(ctx.packageName, true)) {
                val className = resolveInfo.activityInfo.name
                return className
            }
        }
        return ""
    }
}