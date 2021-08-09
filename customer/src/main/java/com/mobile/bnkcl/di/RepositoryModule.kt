package com.mobile.bnkcl.di

import com.mobile.bnkcl.data.api.CommentApi
import com.mobile.bnkcl.data.api.MGApi
import com.mobile.bnkcl.data.repository.comment.CommentRepo
import com.mobile.bnkcl.data.repository.login.LoginRepo
import com.mobile.bnkcl.data.repository.intro.MGRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCommentRepo(commentApi: CommentApi): CommentRepo {
        return CommentRepo(commentApi)
    }

    @Singleton
    @Provides
    fun provideMGRepo(mgApi: MGApi): MGRepo {
        return MGRepo(mgApi)
    }

    @Singleton
    @Provides
    fun provideLoginRepo() : LoginRepo {
        return LoginRepo()
    }
}