package com.mobile.bnkcl.ui.main

import com.bnkc.sourcemodule.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    var userRole = 1
    var isLogin = false
}
