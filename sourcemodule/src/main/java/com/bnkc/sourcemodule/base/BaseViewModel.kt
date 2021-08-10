package com.bnkc.sourcemodule.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel: ViewModel() {

    lateinit var context : Context

    /**
     * it’s important to avoid doing more work than needed as it can waste memory and energy.
     */
    fun cancelRequests() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}