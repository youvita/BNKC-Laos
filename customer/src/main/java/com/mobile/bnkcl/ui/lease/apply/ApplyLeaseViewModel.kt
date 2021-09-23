package com.mobile.bnkcl.ui.lease.apply

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.request.lease.apply.ApplyLeaseRequest
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.lease.ItemResponse
import com.mobile.bnkcl.data.response.lease.ItemResponseObject
import com.mobile.bnkcl.data.response.lease.apply.ApplyLeaseResponse
import com.mobile.bnkcl.data.response.user.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyLeaseViewModel @Inject constructor(private val codesRepo: CodesRepo,private val userRepo: UserRepo, private val leaseRepo: LeaseRepo) : BaseViewModel() {

    private val _actionMuLiveData = MutableLiveData<String>()
    val actionLiveData = _actionMuLiveData

    private val _jobCodesLiveData: MutableLiveData<CodesResponse> = MutableLiveData()
    val jobTypesLiveData: LiveData<CodesResponse> = _jobCodesLiveData
    fun getJobTypeCodes() {
        viewModelScope.launch {
            codesRepo.getCodes("JOB_TYPE").onEach { resource ->
                _jobCodesLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _userProfile: MutableLiveData<ProfileData> = MutableLiveData()
    val userProfileLiveData: LiveData<ProfileData> = _userProfile
    fun getUserProfile() {
        viewModelScope.launch {
            userRepo.getProfile().onEach { resource ->
                _userProfile.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _applyLeaseLiveData: MutableLiveData<ApplyLeaseResponse> = MutableLiveData()
    val applyLeaseLiveData: LiveData<ApplyLeaseResponse> = _applyLeaseLiveData
    var applyLeaseRequest = ApplyLeaseRequest()
    fun applyLease(){
        viewModelScope.launch {
            leaseRepo.applyLease(applyLeaseRequest).onEach { resource ->
                _applyLeaseLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _productTypeMuLiveData : MutableLiveData<ItemResponse> = MutableLiveData<ItemResponse>()
    val productTypeLiveData = _productTypeMuLiveData
    fun reqLeaseItemCode(groupId :String){
        viewModelScope.launch {
            leaseRepo.getItemCode(groupId).onEach { resource ->
                _productTypeMuLiveData.value = resource.data!!
            }.launchIn(viewModelScope)
        }
    }

    private val _repaymentMuLiveData : MutableLiveData<ItemResponse> = MutableLiveData<ItemResponse>()
    val repaymentLiveData = _repaymentMuLiveData
    fun reqRepaymentCode(groupId :String){
        viewModelScope.launch {
            leaseRepo.getItemCode(groupId).onEach { resource ->
                _repaymentMuLiveData.value = resource.data!!
            }.launchIn(viewModelScope)
        }
    }

    private val _typeMuLiveData : MutableLiveData<ItemResponse> = MutableLiveData<ItemResponse>()
    val typeLiveData = _typeMuLiveData
    fun reqTypeCode(groupId :String){
        viewModelScope.launch {
            leaseRepo.getItemCode(groupId).onEach { resource ->
                _typeMuLiveData.value = resource.data!!
            }.launchIn(viewModelScope)
        }
    }

    private val _brandMuLiveData : MutableLiveData<ItemResponse> = MutableLiveData<ItemResponse>()
    val brandLiveData = _brandMuLiveData
    fun reqBrandCode(groupId :String){
        viewModelScope.launch {
            leaseRepo.getItemCode(groupId).onEach { resource ->
                _brandMuLiveData.value = resource.data!!
            }.launchIn(viewModelScope)
        }
    }

    private val _modelMuLiveData : MutableLiveData<ItemResponse> = MutableLiveData<ItemResponse>()
    val modelLiveData = _modelMuLiveData
    fun reqModelCode(groupId :String){
        viewModelScope.launch {
            leaseRepo.getItemCode(groupId).onEach { resource ->
                _modelMuLiveData.value = resource.data!!
            }.launchIn(viewModelScope)
        }
    }

    fun applyLeaseClicked(){
        _actionMuLiveData.value = "apply_lease"
    }

    fun selectProductType(){
        _actionMuLiveData.value = "product_type"
    }

    fun editUserInfo() {
        _actionMuLiveData.value = "edit_info"
    }

    fun selectBrand(){
        _actionMuLiveData.value = "brand"
    }

    fun selectType(){
        _actionMuLiveData.value = "type"
    }

    fun selectModel() {
        _actionMuLiveData.value = "model"
    }

    var productTypes: ArrayList<String>? = ArrayList()
    fun setUpProductTypeData(itemResponse: ArrayList<ItemResponseObject>) : ArrayList<String> {
        productTypes?.clear()
        for (i in 0 until itemResponse.size){
            productTypes?.add(itemResponse[i].title!!)
        }
        return productTypes!!
    }

    var repaymentData: ArrayList<String>? = ArrayList()
    fun setUpRepaymentData(itemResponse: ArrayList<ItemResponseObject>) : ArrayList<String> {
        repaymentData?.clear()
        for (i in 0 until itemResponse.size){
            repaymentData?.add(itemResponse[i].title!!)
        }
        return repaymentData!!
    }

    var typeData: ArrayList<String>? = ArrayList()
    fun setUpTypeData(itemResponse: ArrayList<ItemResponseObject>) : ArrayList<String> {
        typeData?.clear()
        for (i in 0 until itemResponse.size){
            typeData?.add(itemResponse[i].title!!)
        }
        return typeData!!
    }

    var brandData: ArrayList<String>? = ArrayList()
    fun setUpBrandData(itemResponse: ArrayList<ItemResponseObject>) : ArrayList<String> {
        brandData?.clear()
        for (i in 0 until itemResponse.size){
            brandData?.add(itemResponse[i].title!!)
        }
        return brandData!!
    }

    var modelData: ArrayList<String>? = ArrayList()
    fun setUpModelData(itemResponse: ArrayList<ItemResponseObject>) : ArrayList<String> {
        modelData?.clear()
        for (i in 0 until itemResponse.size){
            modelData?.add(itemResponse[i].title!!)
        }
        return modelData!!
    }

    fun repaymentTerm(){
        _actionMuLiveData.value = "repayment_term"
    }

}