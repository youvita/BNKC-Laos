/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.network

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bnkc.library.data.Resource
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class RemoteDataSource<T> @MainThread constructor() {

    val result = MutableLiveData<Resource<T>>()

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    suspend fun networkRequest() {
        withContext(Dispatchers.IO) {
            when (val response = RetrofitRequest.handleNetworkRequest { createCall() }) {
                is RetrofitResponse.Success -> {
                    Log.d("response:: ","Success ${response.body}")

                    val success = "Success Comment Post"
                    RxJava.publish(RxEvent.CommentSuccess(success))
                    setValue(Resource.Success(response.body))
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<T>>

    @MainThread
    protected abstract suspend fun createCall(): Response<T>
}