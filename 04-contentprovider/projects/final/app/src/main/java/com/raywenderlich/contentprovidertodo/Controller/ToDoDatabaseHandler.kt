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

package com.raywenderlich.contentprovidertodo.Controller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.MatrixCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.raywenderlich.contentprovidertodo.Controller.provider.ToDoContract.ALL_ITEMS
import com.raywenderlich.contentprovidertodo.Controller.provider.ToDoContract.CONTENT_PATH
import com.raywenderlich.contentprovidertodo.Model.ToDo
import com.raywenderlich.contentprovidertodo.Model.ToDoDbSchema.DATABASE_NAME
import com.raywenderlich.contentprovidertodo.Model.ToDoDbSchema.DATABASE_VERSION
import com.raywenderlich.contentprovidertodo.Model.ToDoDbSchema.ToDoTable.Columns.KEY_TODO_ID
import com.raywenderlich.contentprovidertodo.Model.ToDoDbSchema.ToDoTable.Columns.KEY_TODO_IS_COMPLETED
import com.raywenderlich.contentprovidertodo.Model.ToDoDbSchema.ToDoTable.Columns.KEY_TODO_NAME
import com.raywenderlich.contentprovidertodo.Model.ToDoDbSchema.ToDoTable.TABLE_NAME


class ToDoDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  //This method creates the database
  override fun onCreate(db: SQLiteDatabase?) {
    val createToDoTable = """
      CREATE TABLE $TABLE_NAME  (
        $KEY_TODO_ID INTEGER PRIMARY KEY,
        $KEY_TODO_NAME  TEXT,
        $KEY_TODO_IS_COMPLETED  LONG);
    """
    db?.execSQL(createToDoTable)
  }

  // This method updates the database
  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    onCreate(db)
  }

  // This method queries the database depending upon the request from the content provider
  fun query(position: Int) : Cursor? {
    lateinit var cursor : Cursor
    var selectAll = "SELECT * FROM $TABLE_NAME"
    if(position != ALL_ITEMS) {
      selectAll = "SELECT * FROM $TABLE_NAME WHERE $KEY_TODO_ID = $position"
    }
    cursor = readableDatabase.rawQuery(selectAll, null)
    return cursor
  }

  // Insert items into the database
  fun insert(toDoName: String) : Long {
    val values = ContentValues()
    values.put(KEY_TODO_NAME, toDoName)
    values.put(KEY_TODO_IS_COMPLETED, false)
    return writableDatabase.insert(TABLE_NAME, null, values)
  }

  // Delete items from the database
  fun delete(id: Long) : Int {
        return writableDatabase.delete(TABLE_NAME, "$KEY_TODO_ID=?",
            arrayOf(id.toString()))
  }

  // Count the number of items from the database
  fun count() : Cursor? {
    val cursor = MatrixCursor(arrayOf(CONTENT_PATH))
    val count =  DatabaseUtils.queryNumEntries(readableDatabase, TABLE_NAME)
    cursor.addRow(arrayOf<Any>(count))
    return cursor
  }

  // Update the items in the database
  fun update(toDo: ToDo): Int {
    val todoId = toDo.toDoId.toString()
    val values = ContentValues()
    values.put(KEY_TODO_NAME, toDo.toDoName)
    values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
    return writableDatabase.update(TABLE_NAME, values, "$KEY_TODO_ID=?", arrayOf(todoId))
  }
}