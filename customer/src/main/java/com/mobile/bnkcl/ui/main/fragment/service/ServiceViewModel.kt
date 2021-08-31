package com.mobile.bnkcl.ui.main.fragment.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bnkc.sourcemodule.base.BaseViewModel

class ServiceViewModel : BaseViewModel() {

    private val _actMuLiveData = MutableLiveData<String>()
    var actLiveData : LiveData<String> = _actMuLiveData

    fun serviceMoreDetail(){
        _actMuLiveData.value = "DETAIL"
    }

}