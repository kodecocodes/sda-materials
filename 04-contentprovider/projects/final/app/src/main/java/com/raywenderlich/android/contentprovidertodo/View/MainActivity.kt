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

package com.raywenderlich.android.contentprovidertodo.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.contentprovidertodo.controller.ToDoAdapter
import com.raywenderlich.android.contentprovidertodo.databinding.ActivityMainBinding
import com.raywenderlich.android.contentprovidertodo.databinding.DialogToDoItemBinding


/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {
  lateinit private var binding: ActivityMainBinding
  private var layoutManager: RecyclerView.LayoutManager? = null
  private lateinit var toDoAdapter : ToDoAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    layoutManager = LinearLayoutManager(this)
    toDoAdapter = ToDoAdapter(this)
    binding.recyclerView.layoutManager = layoutManager
    binding.recyclerView.adapter = toDoAdapter

    binding.fab.setOnClickListener {
      val dialog = AlertDialog.Builder(this)
      dialog.setTitle("Add To Do Item")
      val dialogToDoItemBinding = DialogToDoItemBinding.inflate(LayoutInflater.from(applicationContext))
      dialog.setView(dialogToDoItemBinding.root)
      dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
        if (dialogToDoItemBinding.edtToDoName.text.isNotEmpty()) {
          toDoAdapter.insertToDo(dialogToDoItemBinding.edtToDoName.text.toString())
        }
      }
      dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
      }
      dialog.show()
    }
  }
}
