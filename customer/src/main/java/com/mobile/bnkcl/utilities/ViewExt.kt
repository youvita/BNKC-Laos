package com.mobile.bnkcl.utilities

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * kotlin Extension function
 * **/

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}