package com.mobile.bnkcl.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.area.AreaRepo
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.response.area.AreaItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressInfoViewModel @Inject constructor(private val areaRepo: AreaRepo) : BaseViewModel() {
    var addressData : AddressData? = null
    private val _capitalArea: MutableLiveData<List<AreaItems>> = MutableLiveData()
    val capital: LiveData<List<AreaItems>> get() = _capitalArea

    /**
     * request capital data
     * */
    fun getCapitalData(){
        viewModelScope.launch {
            areaRepo.getCapitalData().onEach { resource ->
                _capitalArea.value = resource.data?.areas
            }.launchIn(viewModelScope)
        }
    }

    /**
     * set up Capital area
     * */
    private var areaNames: ArrayList<String>? = ArrayList()
    fun setUpAreaName(data : ArrayList<AreaItems>) : ArrayList<String> {
        areaNames!!.clear()
        for (i in 0 until data.size){
            areaNames!!.add(data[i].alias1!!)
        }
        return areaNames!!
    }

    /**
     * request address area
     * */
    private val _districtArea : MutableLiveData<List<AreaItems>> = MutableLiveData()
    val address : LiveData<List<AreaItems>> get() = _districtArea

    fun getAddress(){
        viewModelScope.launch {
            areaRepo.getAddressData(addressData!!).onEach { resource ->
                _districtArea.value = resource.data?.areas
            }.launchIn(viewModelScope)
        }
    }
}