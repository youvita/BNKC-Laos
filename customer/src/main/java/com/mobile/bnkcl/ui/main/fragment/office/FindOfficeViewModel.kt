package com.mobile.bnkcl.ui.main.fragment.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.data.repository.findoffice.FindOfficeRepo
import com.mobile.bnkcl.data.request.findoffice.AreaRequest
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.response.area.AreaObjResponse
import com.mobile.bnkcl.data.response.area.BranchResponse
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindOfficeViewModel @Inject constructor(var findOfficeRepo: FindOfficeRepo) : BaseViewModel(){

    var branchRequest: BranchRequest? = null

    private val _areaMuLiveData: MutableLiveData<List<AreaDataResponse>> = MutableLiveData()
    val areaLiveData: LiveData<List<AreaDataResponse>> = _areaMuLiveData
    var areaRequest = AreaRequest("", "BRANCH", "", "PARENT_ID", "1", "10", "")
    fun reqAreasList () {
        viewModelScope.launch {
            findOfficeRepo.getAreas(areaRequest).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _areaMuLiveData.value = resource.data?.areas
//                    var areaObjResponse = resource.data!!.areas[0]
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _branchMuLiveData: MutableLiveData<List<BranchResData>> = MutableLiveData()
    val branchLiveData: LiveData<List<BranchResData>> = _branchMuLiveData
    fun reqBranchList () {
        viewModelScope.launch {
            findOfficeRepo.getBranches(branchRequest!!).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _branchMuLiveData.value = resource.data?.branches
                }
            }.launchIn(viewModelScope)
        }
    }

}