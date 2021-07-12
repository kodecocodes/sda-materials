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
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.contentprovidertodoclient.controller

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.contentprovidertodoclient.controller.provider.ToDoContract.CONTENT_PATH
import com.raywenderlich.android.contentprovidertodoclient.controller.provider.ToDoContract.CONTENT_URI
import com.raywenderlich.android.contentprovidertodoclient.controller.provider.ToDoContract.ROW_COUNT_URI
import com.raywenderlich.android.contentprovidertodoclient.controller.provider.ToDoContract.ToDoTable.Columns.KEY_TODO_ID
import com.raywenderlich.android.contentprovidertodoclient.controller.provider.ToDoContract.ToDoTable.Columns.KEY_TODO_IS_COMPLETED
import com.raywenderlich.android.contentprovidertodoclient.controller.provider.ToDoContract.ToDoTable.Columns.KEY_TODO_NAME
import com.raywenderlich.android.contentprovidertodoclient.model.ToDo
import com.raywenderlich.android.contentprovidertodoclient.R
import com.raywenderlich.android.contentprovidertodoclient.databinding.DialogToDoItemBinding
import com.raywenderlich.android.contentprovidertodoclient.databinding.ToDoListItemBinding


class ToDoAdapter(private val context: Context) :
  RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
  private val queryUri = CONTENT_URI.toString() // base uri
  private val queryCountUri = ROW_COUNT_URI.toString()
  private val projection = arrayOf(CONTENT_PATH) //table
  private var selectionClause: String? = null
  private var selectionArgs: Array<String>? = null
  private val sortOrder = "ASC"

  override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
    val view = ToDoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(view)
  }

  override fun getItemCount(): Int {
    val cursor = context.contentResolver.query(
      Uri.parse(queryCountUri), projection, null,
      null, sortOrder
    )
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        val itemCount = cursor.getInt(0)
        cursor.close()
        return itemCount
      }
    }
    return 0
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    selectionArgs = arrayOf(position.toString())

    val cursor = context.contentResolver.query(
      Uri.parse(queryUri), projection,
      selectionClause,
      selectionArgs, sortOrder
    )
    if (cursor != null) {
      if (cursor.moveToPosition(position)) {
        val toDoId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID))
        val toDoName = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
        val toDoCompleted = cursor.getInt(cursor.getColumnIndex(KEY_TODO_IS_COMPLETED)) > 0
        cursor.close()
        val toDo = ToDo(toDoId, toDoName, toDoCompleted)
        holder.bindViews(toDo)
      }
    }
  }


  inner class ViewHolder(private val binding: ToDoListItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    fun bindViews(toDo: ToDo) {
      binding.txtToDoName.text = toDo.toDoName
      binding.chkToDoCompleted.isChecked = toDo.isCompleted
      binding.chkToDoCompleted.setOnCheckedChangeListener { compoundButton, _ ->
        toDo.isCompleted = compoundButton.isChecked
        val values = ContentValues()
        values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
        values.put(KEY_TODO_ID, toDo.toDoId)
        values.put(KEY_TODO_NAME, toDo.toDoName)
        selectionArgs = arrayOf(toDo.toDoId.toString())
        context.contentResolver.update(
          Uri.parse(queryUri), values, selectionClause,
          selectionArgs
        )
      }

      binding.imgDelete.setOnClickListener(this)
      binding.imgEdit.setOnClickListener(this)
    }


    override fun onClick(imgButton: View?) {
      var position: Int = bindingAdapterPosition
      selectionArgs = arrayOf(position.toString())

      val cursor = context.contentResolver.query(
        Uri.parse(queryUri), projection, selectionClause,
        selectionArgs, sortOrder
      )
      if (cursor != null) {
        if (cursor.moveToPosition(position)) {
          val toDoId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID))
          val toDoName = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
          val toDoCompleted = cursor.getInt(cursor.getColumnIndex(KEY_TODO_IS_COMPLETED)) > 0
          val toDo = ToDo(toDoId, toDoName, toDoCompleted)
          when (imgButton?.id) {
            binding.imgDelete.id -> {
              deleteToDo(toDo.toDoId)
            }
            binding.imgEdit.id -> {
              editToDo(toDo)
            }
          }
        }
      }
    }

    private fun deleteToDo(id: Long) {
      selectionArgs = arrayOf(id.toString())
      Log.e("MESG", "Trying to delete")
      context.contentResolver.delete(Uri.parse(queryUri), selectionClause, selectionArgs)
      notifyDataSetChanged()
    }

    private fun editToDo(toDo: ToDo) {
      val dialog = AlertDialog.Builder(context)
      dialog.setTitle("Update To Do Item")
      val dialogToDoItemBinding = DialogToDoItemBinding.inflate(LayoutInflater.from(context))
      dialogToDoItemBinding.edtToDoName.setText(toDo.toDoName)
      dialog.setView(dialogToDoItemBinding.root)
      dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
        if (dialogToDoItemBinding.edtToDoName.text.isNotEmpty()) {
          selectionArgs = arrayOf(toDo.toDoId.toString())
          var values = ContentValues()
          values.put(KEY_TODO_NAME, dialogToDoItemBinding.edtToDoName.text.toString())
          values.put(KEY_TODO_ID, toDo.toDoId)
          values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
          selectionArgs = arrayOf(toDo.toDoId.toString())
          context.contentResolver.update(
            Uri.parse(queryUri), values, selectionClause,
            selectionArgs
          )
          notifyDataSetChanged()
        }
      }
      dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
      dialog.show()
    }
  }
}