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

package com.raywenderlich.whatsup.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.whatsup.R
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.firebase.cloudStorage.CloudStorageManager
import com.raywenderlich.whatsup.ui.Router
import com.raywenderlich.whatsup.util.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

  private val authenticationManager by lazy { AuthenticationManager() }
  private val cloudStorageManager by lazy { CloudStorageManager() }

  private val router by lazy { Router() }
  private val imageLoader by lazy { ImageLoader() }

  companion object {
    const val CHOOSE_IMAGE_REQUEST_CODE = 123
    fun createIntent(context: Context) = Intent(context, HomeActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    initialize()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean =
      when (item?.itemId) {
        R.id.action_logout -> {
          authenticationManager.signOut(this)
          router.startLoginScreen(this)
          finish()
          true
        }
        else -> super.onOptionsItemSelected(item)
      }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      val selectedImageUri = data?.data ?: return
      cloudStorageManager.uploadPhoto(selectedImageUri, ::onPhotoUploadSuccess)
    }
  }

  private fun initialize() {
    setSupportActionBar(homeToolbar)

    addPostFab.setOnClickListener { router.showImagePicker(this) }
  }

  private fun onPhotoUploadSuccess(url: String) {
    imageLoader.loadImage(this, url, imageView)
  }
}
