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

package com.raywenderlich.whatsup.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.whatsup.databinding.PostItemBinding
import com.raywenderlich.whatsup.model.Post
import com.raywenderlich.whatsup.util.DateUtils

class FeedAdapter(private val dateUtils: DateUtils) :
    RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {

  private val posts = mutableListOf<Post>()
  private val onItemClickLiveData = MutableLiveData<Post>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val view = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(view, onItemClickLiveData, dateUtils)
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
      holder.setItem(posts[position])

  override fun getItemCount(): Int = posts.size

  fun onFeedUpdate(posts: List<Post>) {
    this.posts.clear()
    this.posts.addAll(posts)
    notifyDataSetChanged()
  }

  fun onPostItemClick(): LiveData<Post> = onItemClickLiveData

  class PostViewHolder(
      private val binding: PostItemBinding,
      private val onItemClickLiveData: MutableLiveData<Post>,
      private val dateUtils: DateUtils
  ) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var post: Post

    init {
      binding.root.setOnClickListener { onItemClickLiveData.postValue(post) }
    }

    fun setItem(post: Post) {
      this.post = post
      with(binding) {
        author.text = post.author
        content.text = post.content
        time.text = dateUtils.mapToNormalisedDateText(post.timestamp)
      }
    }
  }
}