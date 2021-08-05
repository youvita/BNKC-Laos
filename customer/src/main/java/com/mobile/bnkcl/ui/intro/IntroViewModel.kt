package com.mobile.bnkcl.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobile.bnkcl.data.MGData
import com.mobile.bnkcl.repository.Intro.MGRepo
import com.bnkc.sourcemodule.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val mgRepo: MGRepo) : BaseViewModel() {

    private val _mgData: MutableLiveData<MGData> = MutableLiveData()
    val mgData: LiveData<MGData> get() = _mgData

    fun getMGData(){
        viewModelScope.launch {
            mgRepo.getMGData().onEach { mgData -> _mgData.value = mgData }.launchIn(viewModelScope)
        }
    }

}