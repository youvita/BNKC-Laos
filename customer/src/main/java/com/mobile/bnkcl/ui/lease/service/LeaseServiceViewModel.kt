package com.mobile.bnkcl.ui.lease.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.area.AreaRepo
import com.mobile.bnkcl.ui.lease.ApplyLeaseActivity
import com.mobile.bnkcl.ui.lease.calculate.LeaseCalculateActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaseServiceViewModel @Inject constructor(areaRepo: AreaRepo) : BaseViewModel() {

    private val _actMuLiveData = MutableLiveData<String>()
    var actLiveData : LiveData<String> = _actMuLiveData

    fun goToCalculate(){
        _actMuLiveData.value = LeaseCalculateActivity::class.simpleName
    }

    fun expandProduct(){
        _actMuLiveData.value = ApplyLeaseActivity::class.simpleName
    }

    fun goToApplyLease(){
        _actMuLiveData.value = ApplyLeaseActivity::class.simpleName
    }

}