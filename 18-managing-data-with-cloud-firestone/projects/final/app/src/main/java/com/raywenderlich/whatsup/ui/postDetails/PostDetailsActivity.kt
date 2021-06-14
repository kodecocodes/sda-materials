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

package com.raywenderlich.whatsup.ui.postDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.whatsup.R
import com.raywenderlich.whatsup.databinding.ActivityPostDetailsBinding
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.firebase.firestore.CloudFirestoreManager
import com.raywenderlich.whatsup.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.raywenderlich.whatsup.model.Comment
import com.raywenderlich.whatsup.model.Post
import com.raywenderlich.whatsup.util.DateUtils
import com.raywenderlich.whatsup.util.showToast

class PostDetailsActivity : AppCompatActivity() {

  private var post: Post? = null

  private val commentsAdapter by lazy { CommentsAdapter(DateUtils()) }
  private val authenticationManager by lazy { AuthenticationManager() }
  private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }
  private val cloudFirestoreManager by lazy { CloudFirestoreManager() }
  private lateinit var postDetailsBinding: ActivityPostDetailsBinding

  companion object {
    private const val POST_EXTRA = "post_extra"
    fun createIntent(context: Context, post: Post) = Intent(context, PostDetailsActivity::class.java).apply {
      putExtra(POST_EXTRA, post)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_post_details)
    initialize()
  }

  override fun onStart() {
    super.onStart()
    listenForComments()
  }

  override fun onStop() {
    super.onStop()
    cloudFirestoreManager.stopListeningForCommentsChanges()
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
    post?.id?.let { postId ->
      cloudFirestoreManager.onCommentsValuesChange(postId)
          .observe(this, Observer(::onCommentsUpdate))
    }
  }

  private fun onCommentsUpdate(comments: List<Comment>) {
    commentsAdapter.onCommentsUpdate(comments)
  }

  private fun initializeClickListener() {
    post?.id?.let { postId ->
      postDetailsBinding.updatePostButton.setOnClickListener {
        cloudFirestoreManager.updatePostContent(
            postId,
            postDetailsBinding.postText.text.toString().trim(),
            ::onPostSuccessfullyUpdated,
            ::onPostUpdateFailed
        )
      }

      postDetailsBinding.deletePostButton.setOnClickListener {
        cloudFirestoreManager.deletePost(postId, ::onPostSuccessfullyDeleted, ::onPostDeleteFailed)
      }

      postDetailsBinding.addCommentButton.setOnClickListener {
        val comment = postDetailsBinding.commentEditText.text.toString().trim()
        if (comment.isNotEmpty()) {
          cloudFirestoreManager.addComment(postId, comment, ::onCommentSuccessfullyAdded, ::onCommentAddFailed)
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