package com.mobile.bnkcl.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel: ViewModel() {

    /**
     * it’s important to avoid doing more work than needed as it can waste memory and energy.
     */
    fun cancelRequests() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}