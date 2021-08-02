package com.mobile.bnkcl.repository.comment

import com.bnkc.library.network.RemoteDataSource
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.data.api.CommentApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CommentRepo(private val commentApi: CommentApi) {

    fun getComments(): Flow<List<CommentsItem>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<List<CommentsItem>>() {
                override suspend fun createCall(): Response<List<CommentsItem>> {
                    return commentApi.getComments(1)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value?.data!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}