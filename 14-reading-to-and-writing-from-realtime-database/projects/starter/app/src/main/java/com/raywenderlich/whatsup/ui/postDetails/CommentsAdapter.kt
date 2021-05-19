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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.whatsup.R
import com.raywenderlich.whatsup.model.Comment
import com.raywenderlich.whatsup.util.DateUtils
import kotlinx.android.synthetic.main.comment_list_item.view.*

class CommentsAdapter(private val dateUtils: DateUtils) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

  private val comments = mutableListOf<Comment>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false)
    return CommentViewHolder(view, dateUtils)
  }

  override fun onBindViewHolder(holder: CommentViewHolder, position: Int) = holder.setItem(comments[position])

  override fun getItemCount(): Int = comments.size

  fun onCommentsUpdate(comments: List<Comment>) {
    this.comments.clear()
    this.comments.addAll(comments)
    notifyDataSetChanged()
  }

  class CommentViewHolder(private val view: View, private val dateUtils: DateUtils) : RecyclerView.ViewHolder(view) {

    fun setItem(comment: Comment) {
      with(view) {
        author.text = comment.author
        timestamp.text = dateUtils.mapToNormalisedDateText(comment.time)
        commentText.text = comment.content
      }
    }
  }
}