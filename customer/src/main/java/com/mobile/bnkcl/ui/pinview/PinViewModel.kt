package com.mobile.bnkcl.ui.pinview

import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.repository.Intro.MGRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(private val mgRepo: MGRepo) : BaseViewModel(){

}