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

package com.raywenderlich.sqlitetodo.Controller

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.sqlitetodo.Model.ToDo
import com.raywenderlich.sqlitetodo.R
import kotlinx.android.synthetic.main.dialog_to_do_item.view.*
import kotlinx.android.synthetic.main.to_do_list_item.view.*


class ToDoAdapter(private val list: ArrayList<ToDo>,
                          private val context: Context) :
                          RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
  private var dialogBuilder: AlertDialog.Builder? = null
  private var dialog: AlertDialog? = null
  private var dbHandler: ToDoDatabaseHandler

  init {
    dbHandler = ToDoDatabaseHandler(context)
  }

  override fun onCreateViewHolder(parent: ViewGroup, position: Int):
                                               ToDoAdapter.ViewHolder {
    val view = LayoutInflater.from(context)
    .inflate(R.layout.to_do_list_item, parent, false)
    return ViewHolder(view)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  fun insertToDo(toDoName: String)
  {
    val toDo = ToDo()
    toDo.toDoName = toDoName
    toDo.isCompleted = false
    dbHandler.createToDo(toDo)
    list.add(toDo)

  }

  override fun onBindViewHolder(holder: ToDoAdapter.ViewHolder, position: Int) {
    holder.bindViews(list[position])
  }

  inner class ViewHolder(itemView: View) :
      RecyclerView.ViewHolder(itemView), View.OnClickListener {

    fun bindViews(toDo: ToDo) {
      itemView.txtToDoName.text = toDo.toDoName
      itemView.chkToDoCompleted.isChecked = toDo.isCompleted

      itemView.chkToDoCompleted.setOnCheckedChangeListener { compoundButton, _ ->
        toDo.isCompleted = compoundButton.isChecked
        dbHandler.updateToDo(toDo)
      }

      itemView.imgDelete.setOnClickListener(this)
      itemView.imgEdit.setOnClickListener(this)
    }


    override fun onClick(imgButton: View?) {
      var position: Int = adapterPosition
      var toDo = list[position]

      when (imgButton!!.id) {
        itemView.imgDelete.id -> {
          deleteToDo(toDo.toDoId)
          list.removeAt(adapterPosition)
          notifyItemRemoved(adapterPosition)
        }
        itemView.imgEdit.id -> {
          editToDo(toDo)
        }
      }


    }

    fun deleteToDo(id: Long) {
      dbHandler.deleteToDo(id)
    }

    fun editToDo(toDo: ToDo) {
      val dialog = AlertDialog.Builder(context)
      dialog.setTitle("Update To Do Item")
      val view = LayoutInflater.from(context).inflate(R.layout.dialog_to_do_item, null)
      view.edtToDoName.setText(toDo.toDoName)
      dialog.setView(view)
      dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
        if (view.edtToDoName.text.isNotEmpty()) {
          toDo.toDoName = view.edtToDoName.text.toString()
          dbHandler.updateToDo(toDo)
          notifyDataSetChanged()
        }
      }
      dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
      dialog.show()
    }
  }
}