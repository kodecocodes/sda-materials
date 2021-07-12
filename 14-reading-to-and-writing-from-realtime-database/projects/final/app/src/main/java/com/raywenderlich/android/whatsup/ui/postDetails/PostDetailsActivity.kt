/*
 * Copyright (c) 2019 Razeware LLC
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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.whatsup.R
import com.raywenderlich.android.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.android.whatsup.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.raywenderlich.android.whatsup.model.Comment
import com.raywenderlich.android.whatsup.model.Post
import com.raywenderlich.android.whatsup.util.DateUtils
import com.raywenderlich.android.whatsup.util.showToast
import kotlinx.android.synthetic.main.activity_post_details.*

class PostDetailsActivity : AppCompatActivity() {

  private lateinit var post: Post

  private val commentsAdapter by lazy { CommentsAdapter(DateUtils()) }
  private val authenticationManager by lazy { AuthenticationManager() }
  private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

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
    realtimeDatabaseManager.removeCommentsValuesChangesListener()
  }

  private fun initialize() {
    setSupportActionBar(postDetailsToolbar)
    extractArguments()
    initializeClickListener()
    initializeRecyclerView()
    if (authenticationManager.getCurrentUser() != post.author) {
      updatePostButton.visibility = View.GONE
      deletePostButton.visibility = View.GONE
    }

    postText.setText(post.content, TextView.BufferType.EDITABLE)
  }

  private fun initializeRecyclerView() {
    commentsRecyclerView.layoutManager = LinearLayoutManager(this)
    commentsRecyclerView.setHasFixedSize(true)
    commentsRecyclerView.adapter = commentsAdapter

    val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    commentsRecyclerView.addItemDecoration(divider)
  }

  private fun listenForComments() {
    realtimeDatabaseManager.onCommentsValuesChange(post.id)
        .observe(this, Observer(::onCommentsUpdate))
  }

  private fun onCommentsUpdate(comments: List<Comment>) {
    commentsAdapter.onCommentsUpdate(comments)
  }

  private fun initializeClickListener() {
    updatePostButton.setOnClickListener {
      realtimeDatabaseManager.updatePostContent(post.id, postText.text.toString().trim())
      finish()
    }

    deletePostButton.setOnClickListener {
      realtimeDatabaseManager.deletePost(post.id)
      finish()
    }

    addCommentButton.setOnClickListener {
      val comment = commentEditText.text.toString().trim()
      if (comment.isNotEmpty()) {
        realtimeDatabaseManager.addComment(post.id, comment)
        commentEditText.text.clear()
      } else {
        showToast(getString(R.string.empty_comment_message))
      }
    }
  }

  private fun extractArguments() {
    val tempPost : Post? = intent.getParcelableExtra(POST_EXTRA)
    tempPost?.let {
      post = it
    }
  }
}