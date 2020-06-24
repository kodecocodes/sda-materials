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

package com.raywenderlich.sqlitetodo.View

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.sqlitetodo.Controller.ToDoAdapter
import com.raywenderlich.sqlitetodo.Controller.ToDoDatabaseHandler
import com.raywenderlich.sqlitetodo.Model.ToDo
import com.raywenderlich.sqlitetodo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_to_do_item.view.*


/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

  private var dbHandler: ToDoDatabaseHandler? = null
  private var toDoList: ArrayList<ToDo>? = null
  private var toDoListItems: ArrayList<ToDo>? = null
  private var layoutManager: RecyclerView.LayoutManager? = null
  private lateinit var toDoAdapter: ToDoAdapter


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    dbHandler = ToDoDatabaseHandler(this)
    toDoList = dbHandler!!.readToDos()
    toDoList = ArrayList<ToDo>()
    toDoListItems = ArrayList()
    layoutManager = LinearLayoutManager(this)
    dbHandler = ToDoDatabaseHandler(this)
    toDoAdapter = ToDoAdapter(dbHandler!!.readToDos(), this)
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = toDoAdapter


    fab.setOnClickListener {
      val dialog = AlertDialog.Builder(this)
      dialog.setTitle("Add To Do Item")
      val view = layoutInflater.inflate(R.layout.dialog_to_do_item, null)
      dialog.setView(view)
      dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
        if (view.edtToDoName.text.isNotEmpty()) {
          toDoAdapter.insertToDo(view.edtToDoName.text.toString())
          toDoAdapter.notifyDataSetChanged()
        }
      }
      dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
      }
      dialog.show()
    }
  }
}
