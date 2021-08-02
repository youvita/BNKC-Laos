/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.app

import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import com.bnkc.library.util.Constants

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