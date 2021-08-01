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

package com.raywenderlich.android.whatsup.ui.addPost

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raywenderlich.android.whatsup.R
import com.raywenderlich.android.whatsup.firebase.realtimeDatabase.RealtimeDatabaseManager
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {

  companion object {
    fun createIntent(context: Context) = Intent(context, AddPostActivity::class.java)
  }

  private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_post)
    initialize()
  }

  private fun initialize() {
    setupClickListeners()
    focusPostMessageInput()
  }

  private fun setupClickListeners() {
    addPostButton.setOnClickListener {
      addPostIfNotEmpty()
    }
  }

  private fun addPostIfNotEmpty() {
    //TODO
  }

  private fun focusPostMessageInput() {
    postText.requestFocus()
  }
}