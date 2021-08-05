package com.mobile.bnkcl.ui.main

import androidx.lifecycle.*
import com.mobile.bnkcl.data.CommentsItem
import com.mobile.bnkcl.repository.comment.CommentRepo
import com.bnkc.sourcemodule.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val commentRepo: CommentRepo) : BaseViewModel() {

    private val _comments: MutableLiveData<List<CommentsItem>> = MutableLiveData()
    val comments: LiveData<List<CommentsItem>> get() = _comments

    fun getComments(){
        viewModelScope.launch {
            commentRepo.getComments().onEach { comments -> _comments.value = comments }.launchIn(viewModelScope)
        }
    }
}
