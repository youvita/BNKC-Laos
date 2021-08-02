package com.mobile.bnkcl.data.api

import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.data.PostItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentApi {

    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Int) : Response<List<CommentsItem>>

    @GET("posts/1")
    suspend fun getPosts() : Response<PostItem>
    
}