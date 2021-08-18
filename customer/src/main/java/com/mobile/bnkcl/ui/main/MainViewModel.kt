package com.mobile.bnkcl.ui.main

import androidx.lifecycle.*
import com.bnkc.library.data.type.Status
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.data.repository.comment.CommentRepo
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.otp.OTPRepo
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import com.mobile.bnkcl.data.response.otp.OTPVerifyResponse
import com.mobile.bnkcl.data.response.otp.SendOTPResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val commentRepo: CommentRepo) : BaseViewModel() {

    var userRole = 1
    var isLogin = false

    private val _comments: MutableLiveData<List<CommentsItem>> = MutableLiveData()
    val comments: LiveData<List<CommentsItem>> get() = _comments

    fun getComments(){
        viewModelScope.launch {
            commentRepo.getComments().onEach { comments -> _comments.value = comments }.launchIn(viewModelScope)
        }
    }
}
