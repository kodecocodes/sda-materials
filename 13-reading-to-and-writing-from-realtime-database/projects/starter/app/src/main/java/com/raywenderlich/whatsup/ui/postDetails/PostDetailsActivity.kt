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

package com.raywenderlich.whatsup.ui.postDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.whatsup.R
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.raywenderlich.whatsup.model.Comment
import com.raywenderlich.whatsup.model.Post
import com.raywenderlich.whatsup.util.DateUtils
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
    //TODO
  }

  private fun onCommentsUpdate(comments: List<Comment>) {
    commentsAdapter.onCommentsUpdate(comments)
  }

  private fun initializeClickListener() {
    updatePostButton.setOnClickListener {
      //TODO
    }

    deletePostButton.setOnClickListener {
      //TODO
    }

    addCommentButton.setOnClickListener {
      //TODO
    }
  }

  private fun extractArguments() {
    post = intent.getParcelableExtra(POST_EXTRA)
  }
}