/*
 * Copyright (c) 2021 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.whatsup.ui.postDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.whatsup.R
import com.raywenderlich.android.whatsup.databinding.ActivityPostDetailsBinding
import com.raywenderlich.android.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.android.whatsup.firebase.firestore.CloudFirestoreManager
import com.raywenderlich.android.whatsup.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.raywenderlich.android.whatsup.model.Comment
import com.raywenderlich.android.whatsup.model.Post
import com.raywenderlich.android.whatsup.util.DateUtils
import com.raywenderlich.android.whatsup.util.showToast

class PostDetailsActivity : AppCompatActivity() {

  private var post: Post? = null

  private val commentsAdapter by lazy { CommentsAdapter(DateUtils()) }
  private val authenticationManager by lazy { AuthenticationManager() }
  private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }
  private val cloudFirestoreManager by lazy { CloudFirestoreManager() }
  private lateinit var postDetailsBinding: ActivityPostDetailsBinding


  companion object {
    private const val POST_EXTRA = "post_extra"
    fun createIntent(context: Context, post: Post) =
      Intent(context, PostDetailsActivity::class.java).apply {
        putExtra(POST_EXTRA, post)
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    postDetailsBinding = ActivityPostDetailsBinding.inflate(layoutInflater)
    setContentView(postDetailsBinding.root)
    initialize()
  }

  override fun onStart() {
    super.onStart()
    listenForComments()
  }

  override fun onStop() {
    super.onStop()
    //TODO
  }

  private fun initialize() {
    setSupportActionBar(postDetailsBinding.postDetailsToolbar)
    extractArguments()
    initializeClickListener()
    initializeRecyclerView()
    if (authenticationManager.getCurrentUser() != post?.author) {
      postDetailsBinding.updatePostButton.visibility = View.GONE
      postDetailsBinding.deletePostButton.visibility = View.GONE
    }

    postDetailsBinding.postText.setText(post?.content, TextView.BufferType.EDITABLE)
  }

  private fun initializeRecyclerView() {
    postDetailsBinding.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
    postDetailsBinding.commentsRecyclerView.setHasFixedSize(true)
    postDetailsBinding.commentsRecyclerView.adapter = commentsAdapter

    val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    postDetailsBinding.commentsRecyclerView.addItemDecoration(divider)
  }

  private fun listenForComments() {
    //TODO
  }

  private fun onCommentsUpdate(comments: List<Comment>) {
    commentsAdapter.onCommentsUpdate(comments)
  }

  private fun initializeClickListener() {
    post?.let { post ->
      postDetailsBinding.updatePostButton.setOnClickListener {
        //TODO
      }

      postDetailsBinding.deletePostButton.setOnClickListener {
        //TODO
      }

      postDetailsBinding.addCommentButton.setOnClickListener {
        val comment = postDetailsBinding.commentEditText.text.toString().trim()
        if (comment.isNotEmpty()) {
          //TODO
        } else {
          showToast(getString(R.string.empty_comment_message))
        }
      }
    }
  }

  private fun onPostSuccessfullyUpdated() {
    showToast(getString(R.string.post_updated_successfully))
    finish()
  }

  private fun onPostUpdateFailed() {
    showToast(getString(R.string.post_update_failed))
  }

  private fun onPostSuccessfullyDeleted() {
    showToast(getString(R.string.post_successfully_deleted))
    finish()
  }

  private fun onPostDeleteFailed() {
    showToast(getString(R.string.post_delete_failed))
  }

  private fun onCommentSuccessfullyAdded() {
    postDetailsBinding.commentEditText.text.clear()
  }

  private fun onCommentAddFailed() {
    showToast(getString(R.string.comment_add_failed))
  }

  private fun extractArguments() {
    post = intent.getParcelableExtra(POST_EXTRA)
  }
}