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

package com.raywenderlich.whatsup.ui

import android.app.Activity
import com.raywenderlich.whatsup.model.Post
import com.raywenderlich.whatsup.ui.addPost.AddPostActivity
import com.raywenderlich.whatsup.ui.home.HomeActivity
import com.raywenderlich.whatsup.ui.login.LoginActivity
import com.raywenderlich.whatsup.ui.postDetails.PostDetailsActivity

class Router {

  fun startHomeScreen(activity: Activity) {
    val intent = HomeActivity.createIntent(activity)
    activity.startActivity(intent)
  }

  fun startLoginScreen(activity: Activity) {
    val intent = LoginActivity.createIntent(activity)
    activity.startActivity(intent)
  }

  fun startAddPostScreen(activity: Activity) {
    val intent = AddPostActivity.createIntent(activity)
    activity.startActivity(intent)
  }

  fun startPostDetailsActivity(activity: Activity, post: Post) {
    val intent = PostDetailsActivity.createIntent(activity, post)
    activity.startActivity(intent)
  }
}