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

package com.raywenderlich.android.whatsup.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.whatsup.R
import com.raywenderlich.android.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.android.whatsup.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.raywenderlich.android.whatsup.model.Post
import com.raywenderlich.android.whatsup.ui.Router
import com.raywenderlich.android.whatsup.util.DateUtils
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

  private val authenticationManager by lazy { AuthenticationManager() }
  private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

  private val router by lazy { Router() }
  private val feedAdapter by lazy { FeedAdapter(DateUtils()) }

  companion object {
    fun createIntent(context: Context) = Intent(context, HomeActivity::class.java)

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    initialize()
  }

  override fun onStart() {
    super.onStart()
    listenForPostsUpdates()
  }

  override fun onStop() {
    super.onStop()
    realtimeDatabaseManager.removePostsValuesChangesListener()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
      when (item.itemId) {
        R.id.action_logout -> {
          authenticationManager.signOut(this)
          router.startLoginScreen(this)
          finish()
          true
        }
        else -> {
          super.onOptionsItemSelected(item)
          true
        }
      }

  private fun initialize() {
    setSupportActionBar(homeToolbar)
    initializeRecyclerView()

    addPostFab.setOnClickListener { router.startAddPostScreen(this) }

    feedAdapter.onPostItemClick()
        .observe(this, Observer(::onPostItemClick))
  }

  private fun initializeRecyclerView() {
    postsFeed.layoutManager = LinearLayoutManager(this)
    postsFeed.setHasFixedSize(true)
    postsFeed.adapter = feedAdapter

    val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    postsFeed.addItemDecoration(divider)
  }

  private fun listenForPostsUpdates() {
    realtimeDatabaseManager.onPostsValuesChange()
        .observe(this, Observer(::onPostsUpdate))
  }

  private fun onPostsUpdate(posts: List<Post>) {
    feedAdapter.onFeedUpdate(posts)
  }

  private fun onPostItemClick(post: Post) {
    router.startPostDetailsActivity(this, post)
  }
}
