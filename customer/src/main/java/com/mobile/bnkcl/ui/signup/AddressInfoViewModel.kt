package com.mobile.bnkcl.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.base.BaseViewModel
import com.bnkc.sourcemodule.data.error.ErrorItem
import com.mobile.bnkcl.data.repository.area.AreaRepo
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.request.auth.IdNumReq
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.area.AreaItems
import com.mobile.bnkcl.data.response.auth.IdNumRes
import com.mobile.bnkcl.data.response.code.CodesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddressInfoViewModel @Inject constructor(private val areaRepo: AreaRepo , private val authRepo: AuthRepo, private val codesRepo: CodesRepo) : BaseViewModel() {

    var isChecked : Boolean = false

    fun agreementCheckBoxClick(){
        isChecked = !isChecked
        Log.d(">>>>>>", "Hello $isChecked")
    }

    private val _capitalArea: MutableLiveData<List<AreaItems>> = MutableLiveData()
    val capital: LiveData<List<AreaItems>> get() = _capitalArea

    /**
     * request capital data
     * */
    fun getCapitalData(){
        try {
            viewModelScope.launch {
                areaRepo.getCapitalData().onEach { resource ->
                    if (resource.status == Status.ERROR) {
                        setError(ErrorItem(null, resource.code, resource.message, null))
                    } else {
                        _capitalArea.value = resource.data?.areas
                    }
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
    fun setUpAreaName(data : List<AreaItems>) : ArrayList<String> {
        areaNames!!.clear()
        for (element in data){
            areaNames!!.add(element.name!!)
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
                    if (resource.status == Status.ERROR) {
                        setError(ErrorItem(null, resource.code, resource.message, null))
                    } else {
                        _districtArea.value = resource.data?.areas
                    }
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
                    if (resource.status == Status.ERROR) {
                        setError(ErrorItem(null, resource.code, resource.message, null))
                    } else {
                        _villageArea.value = resource.data?.areas
                    }
                }.launchIn(viewModelScope)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private val _verifyId : MutableLiveData<IdNumRes> = MutableLiveData()
    val  verifyIdLiveData : LiveData<IdNumRes> = _verifyId
     var idNumReq : IdNumReq? = null
//    var num = SendOTPRequest()
    fun verifyId(){
//        idNumReq = IdNumReq(Constants.USER_ID, idNumReq!!.identification_number )
        viewModelScope.launch {
            authRepo.verifyIdentification(idNumReq!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _verifyId.value = resource.data
                }
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
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _codesLiveData.value = resource.data!!.codes
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * set up job type data
     * */
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
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _genderLiveData.value = resource.data!!.codes
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * set up gender data
     * */
    private var gender : ArrayList<String>? = ArrayList()
    fun setUpGenderCode(data: ArrayList<CodesData>) : ArrayList<String>{
        gender!!.clear()
        for (i in 0 until data?.size){
            gender!!.add(data[i].title!!)
        }
        return gender!!
    }


}