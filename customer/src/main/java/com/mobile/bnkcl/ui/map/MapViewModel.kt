package com.mobile.bnkcl.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.data.repository.findoffice.FindOfficeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONException
import java.lang.String
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val findOfficeRepo : FindOfficeRepo) : BaseViewModel() {

    var branchId: Long = 0

    private var _officeMuLiveData = MutableLiveData<BranchResData>()
    var officeLiveData : LiveData<BranchResData> = _officeMuLiveData
    fun reqOffice() {
        Log.d(">>>>>>", "data :: $branchId")
        viewModelScope.launch {
            findOfficeRepo.getOffice(branchId).onEach { resource ->
                if (resource.status == Status.ERROR) {
                    val code = resource.errorCode
                    val title = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _officeMuLiveData.value = resource.data!!
                }
            }.launchIn(viewModelScope)
        }
    }

}