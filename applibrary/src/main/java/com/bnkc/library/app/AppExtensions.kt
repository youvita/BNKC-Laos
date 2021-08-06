/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.app

import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import com.bnkc.library.util.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Language recreate activity
 */
fun Fragment.recreateLanguageChanged() {
    activity?.let {
        it.intent.putExtra(Constants.CHANGED_LANGUAGE, Constants.CHANGED)
        it.finish()
        startActivity(it.intent)
    }
}

/**
 * Calculate window height for fullscreen
 */
fun Fragment.getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    context?.display?.getRealMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

/**
 * convert any object to string
 */
fun <T> convertToString(obj: T?): String? {
    return Gson().toJson(obj)
}

/**
 * convert any string to object
 */
fun <T> convertToObject(obj: String?): T {
    obj?: emptyList<T>()
    return Gson().fromJson(obj, object : TypeToken<T>() {}.type)
}