/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.bnkc.library.data.network

import android.util.Log.e
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bnkc.library.data.type.ErrorCode
import com.bnkc.library.data.type.Resource
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import io.reactivex.plugins.RxJavaPlugins
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
                    RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)
                    setValue(Resource.Success(response.body))
                }
                is RetrofitResponse.Exception -> {
                    var code = ErrorCode.TIMEOUT_ERROR
                    val throwable = response.throwable
                    if (throwable is CancellationException) {
                        return@withContext
                    } else {
                        if (throwable is UnknownHostException || throwable is ConnectException) {
                            // Unknown Error
                            code = ErrorCode.UNKNOWN_ERROR
                        }
                        withContext(Dispatchers.Main) {
                            RxJava.publish(RxEvent.ServerError(code, "", ""))
                            RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)
                        }
                        setValue(Resource.Error("","", code))
                    }
                }
                is RetrofitResponse.Error -> {
                    when (response.code) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            // Session Expired
                            withContext(Dispatchers.Main) {
                                RxJava.publish(RxEvent.SessionExpired(response.errorTitle!!, response.errorMessage!!))
                                RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)
                            }
                            setValue((Resource.Unauthorized(response.errorTitle!!, response.errorMessage!!)))
                        }
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> {

                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                RxJava.publish(RxEvent.ServerError(response.code, response.errorTitle!!, response.errorMessage!!))
                                RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)
                            }
                            setValue(Resource.Error(response.errorTitle!!,response.errorMessage!!, response.code))
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