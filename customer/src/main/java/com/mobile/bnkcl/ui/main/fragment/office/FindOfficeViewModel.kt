package com.mobile.bnkcl.ui.main.fragment.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.data.repository.findoffice.FindOfficeRepo
import com.mobile.bnkcl.data.request.findoffice.AreaRequest
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindOfficeViewModel @Inject constructor(private var findOfficeRepo: FindOfficeRepo) :
    BaseViewModel() {

    var branchRequest: BranchRequest? = null

    @Inject
    lateinit var sharePref: CredentialSharedPrefer

    private val _areaMuLiveData: MutableLiveData<List<AreaDataResponse>> = MutableLiveData()
    val areaLiveData: LiveData<List<AreaDataResponse>> = _areaMuLiveData
    private var areaRequest = AreaRequest("", "BRANCH", "", "PARENT_ID", "1", "10", "ASC")
    fun reqAreasList() {
        viewModelScope.launch {
            findOfficeRepo.getAreas(areaRequest).onEach { resource ->
                _areaMuLiveData.value = resource.data?.areas
            }.launchIn(viewModelScope)
        }

    }

    private val _branchMuLiveData: MutableLiveData<List<BranchResData>> = MutableLiveData()
    val branchLiveData: LiveData<List<BranchResData>> = _branchMuLiveData
    fun reqBranchList() {
        viewModelScope.launch {
            findOfficeRepo.getBranches(branchRequest!!).onEach { resource ->
                _branchMuLiveData.value = resource.data?.branches
            }.launchIn(viewModelScope)
        }
    }

    private var areaNames: ArrayList<String>? = ArrayList()
    fun setUpAreaName(data: ArrayList<AreaDataResponse>): ArrayList<String> {
        areaNames!!.clear()
        for (i in 0 until data.size) {
            areaNames?.add(data[i].name!!)
        }
        return areaNames!!
    }

}