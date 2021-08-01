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

package com.raywenderlich.android.whatsup.firebase.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raywenderlich.android.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.android.whatsup.model.Comment
import com.raywenderlich.android.whatsup.model.Post

private const val POSTS_COLLECTION = "posts"
private const val COMMENTS_COLLECTION = "comments"

private const val AUTHOR_KEY = "author"
private const val CONTENT_KEY = "content"
private const val ID_KEY = "id"
private const val TIMESTAMP_KEY = "timestamp"
private const val POST_ID = "post_id"

class CloudFirestoreManager {

  private val authenticationManager = AuthenticationManager()

  private val postsValues = MutableLiveData<List<Post>>()
  private val commentsValues = MutableLiveData<List<Comment>>()

  fun addPost(content: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    //TODO
  }

  fun onPostsValuesChange(): LiveData<List<Post>> {
    listenForPostsValueChanges()
    return postsValues
  }

  fun stopListeningForPostChanges() {
    //TODO
  }

  fun updatePostContent(
    key: String,
    content: String,
    onSuccessAction: () -> Unit,
    onFailureAction: () -> Unit
  ) {
    //TODO
  }

  fun deletePost(key: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    //TODO
  }

  fun addComment(
    postId: String,
    content: String,
    onSuccessAction: () -> Unit,
    onFailureAction: () -> Unit
  ) {
    //TODO
  }

  fun onCommentsValuesChange(postId: String): LiveData<List<Comment>> {
    listenForPostCommentsValueChanges(postId)
    return commentsValues
  }

  fun stopListeningForCommentsChanges() {
    //TODO
  }

  private fun deletePostComments(postId: String) {
    //TODO
  }

  private fun listenForPostCommentsValueChanges(postId: String) {
    //TODO
  }

  private fun listenForPostsValueChanges() {
    //TODO
  }

  private fun getCurrentTime() = System.currentTimeMillis()
}