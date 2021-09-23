package com.mobile.bnkcl.ui.pinview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bnkc.sourcemodule.base.BaseViewModel
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.user.UserRepo
import com.mobile.bnkcl.data.request.auth.*
import com.mobile.bnkcl.data.request.user.PreChangeRequest
import com.mobile.bnkcl.data.request.user.ResetPasswordRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.signup.SignUpResponse
import com.mobile.bnkcl.data.response.user.resetpassword.PreChangeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(private val userRepo: UserRepo, private val authRepo: AuthRepo) : BaseViewModel(){

    var signUpRequest : SignUpRequest? = null

    private val _login: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginLiveData: LiveData<LoginResponse> = _login
    var loginRequestNoAuth: LoginRequestNoAuth? = null
    fun loginNoAuth(){
        viewModelScope.launch {
            authRepo.loginUserNoAuth(loginRequestNoAuth!!).onEach { resource ->
                if (resource.data != null)_login.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    var logRequest: LoginRequest? = null
    fun loginWithAuth(){
        viewModelScope.launch {
            authRepo.loginUser(logRequest!!).onEach { resource ->
                if (resource.data != null) _login.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _signUpLiveData : MutableLiveData<SignUpResponse> = MutableLiveData()
    val signUpLiveData: LiveData<SignUpResponse> = _signUpLiveData
    fun signUp(){
        viewModelScope.launch {
            authRepo.signUpUser(signUpRequest!!).onEach { resource ->
                _signUpLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _logout: MutableLiveData<Unit> = MutableLiveData()
    val logoutLiveData: LiveData<Unit> = _logout
    fun logout() {
        viewModelScope.launch {
            userRepo.logout().onEach { resource ->
                _logout.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _preResetMuLiveData: MutableLiveData<PreChangeResponse> = MutableLiveData()
    val preResetLiveData: LiveData<PreChangeResponse> = _preResetMuLiveData
    var preChangeRequest = PreChangeRequest()
    fun preResetPassword(){
        viewModelScope.launch {
            userRepo.preChangePassword(preChangeRequest).onEach { resource ->
                _preResetMuLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _resetMuLiveData: MutableLiveData<Unit> = MutableLiveData()
    val resetLiveData: LiveData<Unit> = _resetMuLiveData
    var resetPasswordRequest = ResetPasswordRequest()
    fun resetPassword(){
        viewModelScope.launch {
            userRepo.resetPassword(resetPasswordRequest).onEach { resource ->
                _resetMuLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

    private val _forgetPinMuLiveData: MutableLiveData<Unit> = MutableLiveData()
    val forgetPinLiveData: LiveData<Unit> = _forgetPinMuLiveData
    var forgetPinRequest = ForgetPinRequest()
    fun forgetPIN(){
        viewModelScope.launch {
            authRepo.forgetPin(forgetPinRequest).onEach { resource ->
                _resetMuLiveData.value = resource.data
            }.launchIn(viewModelScope)
        }
    }

}