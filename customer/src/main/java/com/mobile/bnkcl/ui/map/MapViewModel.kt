package com.mobile.bnkcl.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.sourcemodule.base.BaseViewModel
import com.bnkc.sourcemodule.data.error.ErrorItem
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.data.repository.findoffice.FindOfficeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.String
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val findOfficeRepo : FindOfficeRepo) : BaseViewModel() {

    private var _officeMuLiveData = MutableLiveData<BranchResData>()
    var officeLiveData : LiveData<BranchResData> = _officeMuLiveData
    fun reqOffice(branchId : Long) {
        viewModelScope.launch {
            findOfficeRepo.getOffice(branchId).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    setError(ErrorItem(null, resource.code, resource.message, null))
                } else {
                    _officeMuLiveData.value = resource.data!!
                }
            }.launchIn(viewModelScope)
        }
    }
}