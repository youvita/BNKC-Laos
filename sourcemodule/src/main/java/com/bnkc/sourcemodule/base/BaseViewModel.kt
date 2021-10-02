package com.bnkc.sourcemodule.base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnkc.sourcemodule.data.error.ErrorItem
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel: ViewModel() {

    lateinit var context : Context

    private val _error = MutableLiveData<ErrorItem>()
    var handleError: LiveData<ErrorItem> = _error

    fun setError(errorItem: ErrorItem) {
        _error.value = errorItem
    }

    /**
     * it’s important to avoid doing more work than needed as it can waste memory and energy.
     */
    fun cancelRequests() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}