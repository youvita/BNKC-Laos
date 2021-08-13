package com.mobile.bnkcl.data.repository.signup

import android.webkit.PermissionRequest
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.mobile.bnkcl.data.api.signup.SignUpApi
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception

class SignUpRepo(private val signUpApi: SignUpApi) {
    fun getPreSignUpData() : Flow<Resource<PreSignUpResponse>> = flow{
        try {
            val request = object : RemoteDataSource<PreSignUpResponse>(){
                override suspend fun createCall(): Response<PreSignUpResponse> {
                    var preSignUpRequest = PreSignUpRequest()
                    preSignUpRequest.pin_id = "95f562bb-71bf-4547-9d4b-4e2c7a29a1f2"
                    preSignUpRequest.username = "123456"
                    return signUpApi.setPreSignUpRequest(preSignUpRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}