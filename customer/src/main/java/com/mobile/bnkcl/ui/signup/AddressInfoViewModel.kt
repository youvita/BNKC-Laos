package com.mobile.bnkcl.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.area.AreaRepo
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.request.auth.IdNumReq
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.area.AreaItems
import com.mobile.bnkcl.data.response.auth.IdNumRes
import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.code.CodesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddressInfoViewModel @Inject constructor(private val areaRepo: AreaRepo , private val authRepo: AuthRepo, private val codesRepo: CodesRepo) : BaseViewModel() {
    val _IdNumberContent = MutableLiveData<String>()
    val phoneNumber : LiveData<String>
        get() = _IdNumberContent


    private val _capitalArea: MutableLiveData<List<AreaItems>> = MutableLiveData()
    val capital: LiveData<List<AreaItems>> get() = _capitalArea

    /**
     * request capital data
     * */
    fun getCapitalData(){
        try {
            viewModelScope.launch {
                areaRepo.getCapitalData().onEach { resource ->
                    _capitalArea.value = resource.data?.areas
                }.launchIn(viewModelScope)
            }
        }catch (e:Exception){
            e.printStackTrace()
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
     * request District area
     * */
    private val _districtArea : MutableLiveData<List<AreaItems>> = MutableLiveData()
    val district : LiveData<List<AreaItems>> get() = _districtArea
    var addressData : AddressData? = null

    fun getDistrict(){
        try {
            viewModelScope.launch {
                areaRepo.getAddressData(addressData!!).onEach { resource ->
                    _districtArea.value = resource.data?.areas
                }.launchIn(viewModelScope)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    /**
     * request District area
     * */
    private val _villageArea : MutableLiveData<List<AreaItems>> = MutableLiveData()
    val village : LiveData<List<AreaItems>> get() = _villageArea
    var villageData : AddressData? = null

    fun getVillage(){
        try {
            viewModelScope.launch {
                areaRepo.getAddressData(villageData!!).onEach { resource ->
                    _villageArea.value = resource.data?.areas
                }.launchIn(viewModelScope)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private val _verifyId : MutableLiveData<IdNumRes> = MutableLiveData()
    val  verifyIdLiveData : LiveData<IdNumRes> = _verifyId
     var idNumReq : IdNumReq? = null
    var num = SendOTPRequest()
    fun verifyId(){
        idNumReq = IdNumReq(num.to.toString(), idNumReq!!.identification_number )
        viewModelScope.launch {
            authRepo.verifyIdentification(idNumReq!!).onEach { resource ->
                _verifyId.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    /**
     * get job type
     * */
    private val _codesLiveData: MutableLiveData<List<CodesData>> = MutableLiveData()
    val codesLiveData: LiveData<List<CodesData>> = _codesLiveData
    fun getCodes(group_id: String) {
        viewModelScope.launch {
            codesRepo.getCodes(group_id).onEach { resource ->
                    _codesLiveData.value = resource.data!!.codes

            }.launchIn(viewModelScope)
        }
    }

    private var name : ArrayList<String>? = ArrayList()
    fun setUpCode(data: ArrayList<CodesData>) : ArrayList<String>{
        name!!.clear()
        for (i in 0 until data.size){
            name!!.add(data[i].title!!)
        }
        Log.d("object code", "setUpJobType: ${data.size}")
        return name!!
    }


    /**
     * get gender
     * */
    private val _genderLiveData: MutableLiveData<List<CodesData>> = MutableLiveData()
    val genderLiveData: LiveData<List<CodesData>> = _genderLiveData
    fun getGender(group_id: String) {
        viewModelScope.launch {
            codesRepo.getCodes(group_id).onEach { resource ->
                    _genderLiveData.value = resource.data!!.codes

            }.launchIn(viewModelScope)
        }
    }

    private var gender : ArrayList<String>? = ArrayList()
    fun setUpGenderCode(data: ArrayList<CodesData>) : ArrayList<String>{
        gender!!.clear()

        for (i in 0 until data?.size){
            gender!!.add(data[i].title!!)
        }
        return gender!!
    }
}