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

package com.raywenderlich.whatsup.firebase.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.model.Comment
import com.raywenderlich.whatsup.model.Post

private const val POSTS_COLLECTION = "posts"
private const val COMMENTS_COLLECTION = "comments"

private const val AUTHOR_KEY = "author"
private const val CONTENT_KEY = "content"
private const val ID_KEY = "id"
private const val TIMESTAMP_KEY = "timestamp"
private const val POST_ID = "post_id"

class CloudFirestoreManager {

  private val authenticationManager = AuthenticationManager()
  private val database = FirebaseFirestore.getInstance()

  private val postsValues = MutableLiveData<List<Post>>()
  private val commentsValues = MutableLiveData<List<Comment>>()

  private lateinit var postsRegistration: ListenerRegistration
  private lateinit var commentsRegistration: ListenerRegistration

  fun addPost(content: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    //1
    val documentReference = database.collection(POSTS_COLLECTION).document()

    //2
    val post = HashMap<String, Any>()

    //3
    post[AUTHOR_KEY] = authenticationManager.getCurrentUser()
    post[CONTENT_KEY] = content
    post[TIMESTAMP_KEY] = getCurrentTime()
    post[ID_KEY] = documentReference.id

    //4
    documentReference
        .set(post)
        .addOnSuccessListener { onSuccessAction() }
        .addOnFailureListener { onFailureAction() }
  }

  fun onPostsValuesChange(): LiveData<List<Post>> {
    listenForPostsValueChanges()
    return postsValues
  }

  fun stopListeningForPostChanges() = postsRegistration.remove()

  fun updatePostContent(key: String, content: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    //1
    val updatedPost = HashMap<String, Any>()

    //2
    updatedPost[CONTENT_KEY] = content

    //3
    database.collection(POSTS_COLLECTION)
        .document(key)
        .update(updatedPost)
        .addOnSuccessListener { onSuccessAction() }
        .addOnFailureListener { onFailureAction() }
  }

  fun deletePost(key: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    database.collection(POSTS_COLLECTION)
        .document(key)
        .delete()
        .addOnSuccessListener { onSuccessAction() }
        .addOnFailureListener { onFailureAction() }

    deletePostComments(key)
  }

  fun addComment(postId: String, content: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
    // 1
    val commentReference = database.collection(COMMENTS_COLLECTION).document()

    // 2
    val comment = HashMap<String, Any>()

    // 3
    comment[AUTHOR_KEY] = authenticationManager.getCurrentUser()
    comment[CONTENT_KEY] = content
    comment[POST_ID] = postId
    comment[TIMESTAMP_KEY] = getCurrentTime()

    // 4
    commentReference
        .set(comment) // 5
        .addOnSuccessListener { onSuccessAction() } // 6
        .addOnFailureListener { onFailureAction() } // 7
  }

  fun onCommentsValuesChange(postId: String): LiveData<List<Comment>> {
    listenForPostCommentsValueChanges(postId)
    return commentsValues
  }

  fun stopListeningForCommentsChanges() = commentsRegistration.remove()

  private fun deletePostComments(postId: String) {
    // 1
    database.collection(COMMENTS_COLLECTION)
        .whereEqualTo(POST_ID, postId)
        //2
        .get()
        //3
        .continueWith { task -> task.result?.documents?.forEach { it.reference.delete() } }
  }

  private fun listenForPostCommentsValueChanges(postId: String) {
    // 1
    commentsRegistration = database.collection(COMMENTS_COLLECTION)
        // 2
        .whereEqualTo(POST_ID, postId) // 3
        // 4
        .addSnapshotListener(EventListener<QuerySnapshot> { value, error ->
          if (error != null || value == null) {
            return@EventListener
          }

          if (value.isEmpty) {
            postsValues.postValue(emptyList())
          } else {
            val comments = ArrayList<Comment>()
            for (doc in value) {
              val comment = doc.toObject(Comment::class.java)
              comments.add(comment)
            }
            commentsValues.postValue(comments)
          }
        })
  }

  private fun listenForPostsValueChanges() {
    // 1
    postsRegistration = database.collection(POSTS_COLLECTION) // 2
        // 3
        .addSnapshotListener(EventListener<QuerySnapshot> { value, error ->
          // 4
          if (error != null || value == null) {
            return@EventListener
          }

          // 5
          if (value.isEmpty) {
            // 6
            postsValues.postValue(emptyList())
          } else {
            // 7
            val posts = ArrayList<Post>()
            // 8
            for (doc in value) {
              // 9
              val post = doc.toObject(Post::class.java)
              posts.add(post)
            }
            // 10
            postsValues.postValue(posts)
          }
        })
  }

  private fun getCurrentTime() = System.currentTimeMillis()
}