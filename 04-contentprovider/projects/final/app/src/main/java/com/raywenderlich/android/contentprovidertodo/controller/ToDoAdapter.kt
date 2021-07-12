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

package com.raywenderlich.android.contentprovidertodo.controller

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.contentprovidertodo.controller.provider.ToDoContract.CONTENT_PATH
import com.raywenderlich.android.contentprovidertodo.controller.provider.ToDoContract.CONTENT_URI
import com.raywenderlich.android.contentprovidertodo.controller.provider.ToDoContract.ROW_COUNT_URI
import com.raywenderlich.android.contentprovidertodo.controller.provider.ToDoContract.ToDoTable.Columns.KEY_TODO_ID
import com.raywenderlich.android.contentprovidertodo.controller.provider.ToDoContract.ToDoTable.Columns.KEY_TODO_IS_COMPLETED
import com.raywenderlich.android.contentprovidertodo.controller.provider.ToDoContract.ToDoTable.Columns.KEY_TODO_NAME
import com.raywenderlich.android.contentprovidertodo.model.ToDo
import kotlinx.android.synthetic.main.dialog_to_do_item.view.*
import kotlinx.android.synthetic.main.to_do_list_item.view.*




class ToDoAdapter(private val context: Context) :
                          RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
  private val queryUri = CONTENT_URI.toString() // base uri
  private val queryCountUri = ROW_COUNT_URI.toString()
  private val projection = arrayOf(CONTENT_PATH) //table
  private var selectionClause: String? = null
  private var selectionArgs: Array<String>? = null
  private val sortOrder = "ASC"

 override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
    val view = LayoutInflater.from(context)
    .inflate(com.raywenderlich.android.contentprovidertodo.R.layout.to_do_list_item, parent, false)
    return ViewHolder(view)
  }

  // Get the count of all items
  override fun getItemCount() : Int {
    // Get the number of records from the Content Resolver
    val cursor = context.contentResolver.query(Uri.parse(queryCountUri),projection, selectionClause,
        selectionArgs,sortOrder)
    // Return the count of records
    if(cursor != null) {
      if(cursor.moveToFirst()) {
        val itemCount = cursor.getInt(0)
        cursor.close()
        return itemCount
      }
    }
    return 0
  }

  // Row by row, populate the RecyclerView
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    // 1
    val cursor = context.contentResolver.query(Uri.parse(queryUri),
        projection,
        selectionClause,
        selectionArgs, sortOrder)
    // 2
    if(cursor != null) {
      if(cursor.moveToPosition(position)) {
        val toDoId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID))
        val toDoName = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
        val toDoCompleted = cursor.getInt(cursor.getColumnIndex(KEY_TODO_IS_COMPLETED)) > 0
        cursor.close()
        val toDo = ToDo(toDoId, toDoName, toDoCompleted)
        holder.bindViews(toDo)
      }
    }
  }

  //Insert a To-Do item from the ContentResolver
  fun insertToDo(toDoName: String) {
    // 1
    val values = ContentValues()
    values.put(KEY_TODO_NAME, toDoName)
    // 2
    context.contentResolver.insert(CONTENT_URI, values)
  }


  inner class ViewHolder(itemView: View) :
      RecyclerView.ViewHolder(itemView), View.OnClickListener {

    // Bind the To-Do Item fields to the controls in the RecyclerView row
    fun bindViews(toDo: ToDo) {
      itemView.txtToDoName.text = toDo.toDoName
      itemView.chkToDoCompleted.isChecked = toDo.isCompleted
      itemView.chkToDoCompleted.setOnCheckedChangeListener { compoundButton, _ ->
        toDo.isCompleted = compoundButton.isChecked
        val values = ContentValues()
        values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
        values.put(KEY_TODO_ID, toDo.toDoId)
        values.put(KEY_TODO_NAME, toDo.toDoName)
        selectionArgs = arrayOf(toDo.toDoId.toString())
        context.contentResolver.update(Uri.parse(queryUri), values, selectionClause,
            selectionArgs)
      }

      itemView.imgDelete.setOnClickListener(this)
      itemView.imgEdit.setOnClickListener(this)
    }


    // Handle clicks on the RecyclerView rows
    override fun onClick(imgButton: View?) {
      val cursor = context.contentResolver.query(Uri.parse(queryUri),projection,selectionClause,
          selectionArgs, sortOrder)

      if(cursor != null) {
        if(cursor.moveToPosition(bindingAdapterPosition)) {
          val toDoId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID))
          val toDoName = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
          val toDoCompleted = cursor.getInt(cursor.getColumnIndex(KEY_TODO_IS_COMPLETED)) > 0
          cursor.close()
          val toDo = ToDo(toDoId, toDoName, toDoCompleted)
          when (imgButton?.id) {
            itemView.imgDelete.id -> {
              deleteToDo(toDo.toDoId)
            }
            itemView.imgEdit.id -> {
              editToDo(toDo)
            }
          }
        }
      }
    }

    // Delete a To-Do item from the ContentResolver
    private fun deleteToDo(id: Long) {
      // 1
      selectionArgs = arrayOf(id.toString())
      // 2
      context.contentResolver.delete(Uri.parse(queryUri), selectionClause, selectionArgs)
      // 3
      notifyDataSetChanged()
    }



    // Edit an Item in the ContentResolver
    private fun editToDo(toDo: ToDo) {
      val dialog = AlertDialog.Builder(context)
      dialog.setTitle("Update To Do Item")
      val view = LayoutInflater.from(context).inflate(com.raywenderlich.android.contentprovidertodo.R.layout.dialog_to_do_item, null)
      view.edtToDoName.setText(toDo.toDoName)
      dialog.setView(view)
      dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
        if (view.edtToDoName.text.isNotEmpty()) {
          // 1
          val values = ContentValues()
          values.put(KEY_TODO_NAME,view.edtToDoName.text.toString())
          values.put(KEY_TODO_ID, toDo.toDoId)
          values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
          // 2
          context.contentResolver.update(Uri.parse(queryUri), values, selectionClause,
              selectionArgs)
          // 3
          notifyDataSetChanged()
        }
      }
      dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
      dialog.show()
    }
  }
}