/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bnkc.library.R
import com.bnkc.library.data.Resource
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException

abstract class RemoteDataSource<T> @MainThread constructor() {

    private val result = MutableLiveData<Resource<T>>()

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    suspend fun networkRequest() {
        setValue(Resource.Loading())

        withContext(Dispatchers.IO) {
            when (val response = RetrofitRequest.handleNetworkRequest { createCall() }) {
                is RetrofitResponse.Success -> {
                    RxJava.publish(RxEvent.ResponseSuccess())
                    setValue(Resource.Success(response.body))
                }
                is RetrofitResponse.Exception -> {
                    val throwable = response.throwable
                    if (throwable is CancellationException) {
                        return@withContext
                    } else {
                        if (throwable is UnknownHostException || throwable is ConnectException) {
                            // TODO: error title & message
                            setValue(Resource.Error(R.string.title_no_network, R.string.message_pls_check_network))
                        }
                    }
                }
                is RetrofitResponse.Error -> {
                    when (response.code) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            // TODO: Session Expired
                        }
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> {

                        }
                        else -> {
                            if (response.msg != null) {

                            }
                        }
                    }
                }

            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<T>>

    @MainThread
    protected abstract suspend fun createCall(): Response<T>
}