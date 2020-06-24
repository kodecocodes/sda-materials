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

package com.raywenderlich.whatsup.firebase.realtimeDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.model.Comment
import com.raywenderlich.whatsup.model.Post

private const val POSTS_REFERENCE = "posts"
private const val POST_CONTENT_PATH = "content"
private const val COMMENTS_REFERENCE = "comments"
private const val COMMENT_POST_ID_PATH = "postId"

class RealtimeDatabaseManager {

  private val authenticationManager = AuthenticationManager()
  private val database = FirebaseDatabase.getInstance()

  private val postsValues = MutableLiveData<List<Post>>()
  private val commentsValues = MutableLiveData<List<Comment>>()

  private lateinit var postsValueEventListener: ValueEventListener
  private lateinit var commentsValueEventListener: ValueEventListener

  fun addPost(content: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    //1
    val postsReference = database.getReference(POSTS_REFERENCE)
    //2
    val key = postsReference.push().key ?: ""
    val post = createPost(key, content)

    //3
    postsReference.child(key)
      .setValue(post)
      .addOnSuccessListener { onSuccessAction() }
      .addOnFailureListener { onFailureAction() }
  }

  fun onPostsValuesChange(): LiveData<List<Post>> {
    listenForPostsValueChanges()
    return postsValues
  }

  fun removePostsValuesChangesListener() {
    database.getReference(POSTS_REFERENCE).removeEventListener(postsValueEventListener)
  }

  fun updatePostContent(key: String, content: String) {
    //1
    database.getReference(POSTS_REFERENCE)
        //2
        .child(key)
        //3
        .child(POST_CONTENT_PATH)
        //4
        .setValue(content)
  }

  fun deletePost(key: String) {
    database.getReference(POSTS_REFERENCE)
        .child(key)
        .removeValue()

    deletePostComments(key)
  }

  fun addComment(postId: String, content: String) {
    val commentsReference = database.getReference(COMMENTS_REFERENCE)
    val key = commentsReference.push().key ?: ""
    val comment = createComment(postId, content)

    commentsReference.child(key).setValue(comment)
  }

  fun onCommentsValuesChange(postId: String): LiveData<List<Comment>> {
    listenForPostCommentsValueChanges(postId)
    return commentsValues
  }

  fun removeCommentsValuesChangesListener() {
    database.getReference(COMMENTS_REFERENCE).removeEventListener(commentsValueEventListener)
  }

  private fun listenForPostCommentsValueChanges(postId: String) {
    commentsValueEventListener = object : ValueEventListener {
      override fun onCancelled(databaseError: DatabaseError) {
        /* No op */
      }

      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
          val comments = dataSnapshot.children.mapNotNull { it.getValue(Comment::class.java) }.toList()
          commentsValues.postValue(comments)
        } else {
          commentsValues.postValue(emptyList())
        }
      }
    }

    database.getReference(COMMENTS_REFERENCE)
        .orderByChild(COMMENT_POST_ID_PATH)
        .equalTo(postId)
        .addValueEventListener(commentsValueEventListener)
  }

  private fun createPost(key: String, content: String): Post {
    val user = authenticationManager.getCurrentUser()
    val timestamp = getCurrentTime()
    return Post(key, content, user, timestamp)
  }

  private fun deletePostComments(postId: String) {
    database.getReference(COMMENTS_REFERENCE)
        .orderByChild(COMMENT_POST_ID_PATH)
        .equalTo(postId)
        .addListenerForSingleValueEvent(object : ValueEventListener {
          override fun onCancelled(databaseError: DatabaseError) {
            /* No op */
          }

          override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataSnapshot.children.forEach { it.ref.removeValue() }
          }
        })
  }

  private fun listenForPostsValueChanges() {
    //1
    postsValueEventListener = object : ValueEventListener {
      //2
      override fun onCancelled(databaseError: DatabaseError) {
        /* No op */
      }

      //3
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        //4
        if (dataSnapshot.exists()) {
          val posts = dataSnapshot.children.mapNotNull { it.getValue(Post::class.java) }.toList()
          postsValues.postValue(posts)
        } else {
          //5
          postsValues.postValue(emptyList())
        }
      }
    }

    //6
    database.getReference(POSTS_REFERENCE)
        .addValueEventListener(postsValueEventListener)
  }

  private fun createComment(postId: String, content: String): Comment {
    val user = authenticationManager.getCurrentUser()
    val timestamp = getCurrentTime()
    return Comment(postId, user, timestamp, content)
  }

  private fun getCurrentTime() = System.currentTimeMillis()
}