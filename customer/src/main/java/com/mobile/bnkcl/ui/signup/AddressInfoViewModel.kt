package com.mobile.bnkcl.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.area.AreaRepo
import com.mobile.bnkcl.data.repository.signup.SignUpRepo
import com.mobile.bnkcl.data.response.area.CapitalData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressInfoViewModel @Inject constructor(private val areaRepo: AreaRepo) : BaseViewModel() {

    //Capital Data
    private val _capitalData: MutableLiveData<CapitalData> = MutableLiveData()
    val capital: LiveData<CapitalData> get() = _capitalData

    fun getCapitalData(){
        viewModelScope.launch {
            areaRepo.getCapitalData().onEach { resource ->
                // catch error
                if (resource.status == Status.ERROR) {
                    val code    = resource.errorCode
                    val title   = resource.messageTitle
                    val message = resource.messageDes
                    RxJava.publish(RxEvent.ServerError(code!!, title!!, message!!))
                } else {
                    _capitalData.value = resource.data
                }
            }.launchIn(viewModelScope)
        }
    }
}