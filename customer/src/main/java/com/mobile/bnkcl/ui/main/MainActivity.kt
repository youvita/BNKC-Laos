package com.mobile.bnkcl.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bnkc.library.rxjava.RxEvent
import com.bnkc.library.rxjava.RxJava
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityMainBinding
import com.mobile.bnkcl.ui.adapter.CommentAdapter
import com.bnkc.sourcemodule.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    private var commentDisposable: Disposable? = null


    @Inject
    lateinit var commentAdapter: CommentAdapter

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getCommentList()

        commentDisposable = RxJava.listen(RxEvent.CommentSuccess::class.java).subscribe {
            Log.d(">>>>","Result::: ${it.value}")
        }
    }

    /**
     * get comments
     */
    private fun getCommentList() {
        binding.rvComment.adapter = commentAdapter
        viewModel.getComments()

        viewModel.comments.observe(this) {
            if (it.isNullOrEmpty()) return@observe
            commentAdapter.addItemList(it)
            viewModel.cancelRequests()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        commentDisposable?.dispose()
        commentDisposable = null
    }

}