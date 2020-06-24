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

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.raywenderlich.sqlitetodo.Model.ToDo
import com.raywenderlich.sqlitetodo.Model.ToDoDbSchema.DATABASE_NAME
import com.raywenderlich.sqlitetodo.Model.ToDoDbSchema.DATABASE_VERSION
import com.raywenderlich.sqlitetodo.Model.ToDoDbSchema.ToDoTable.Columns.KEY_TODO_IS_COMPLETED
import com.raywenderlich.sqlitetodo.Model.ToDoDbSchema.ToDoTable.Columns.KEY_TODO_NAME
import com.raywenderlich.sqlitetodo.Model.ToDoDbSchema.ToDoTable.TABLE_NAME

class ToDoDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  //This method creates the database
  override fun onCreate(db: SQLiteDatabase?) {
    // 1
    val createToDoTable = """
      CREATE TABLE $TABLE_NAME  (
        $_ID INTEGER PRIMARY KEY,
        $KEY_TODO_NAME  TEXT,
        $KEY_TODO_IS_COMPLETED  LONG );
    """
    // 2
    db?.execSQL(createToDoTable)
  }


  //This method is used to update the database
  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    //Delete the old table
    // 1
    db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    // 2
    onCreate(db)
  }

  //Create a row in the table to store a to-do item
  fun createToDo(toDo: ToDo) {
    // 1
    // Create a variable of the database and make it writeable
    val db: SQLiteDatabase = writableDatabase

    // 2
    //Content values is a HASHMAP, it stores
    //Key/Value pairs relating to the database
    val values = ContentValues()

    //put a key/value entry into the HashMap
    values.put(KEY_TODO_NAME, toDo.toDoName)
    values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)

    // 3
    //insert the values into the database
    db.insert(TABLE_NAME, null, values)

    //4
    //Close the database
    db.close()
  }


  fun readToDos(): ArrayList<ToDo> {
    // 1
    val db: SQLiteDatabase = readableDatabase
    // 2
    val list = ArrayList<ToDo>()
    // 3
    val selectAll = "SELECT * FROM $TABLE_NAME"
    // 4
    val cursor: Cursor = db.rawQuery(selectAll, null)
    // 5
    if (cursor.moveToFirst()) {
      do {
        // 6
        val toDo = ToDo().apply {
          toDoId = cursor.getLong(cursor.getColumnIndex(_ID))
          toDoName = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
          isCompleted = cursor.getInt(cursor.getColumnIndex(KEY_TODO_IS_COMPLETED)) == 1
        }
        // 7
        list.add(toDo)
      } while (cursor.moveToNext())
    }
    // 8
    cursor.close()
    // 9
    return list
  }

  fun updateToDo(toDo: ToDo): Int {
    // 1
    val todoId = toDo.toDoId.toString()

    // 2
    //Create a variable of the database and make it writeable
    val db: SQLiteDatabase = writableDatabase

    // 3
    //Content values is a HASHMAP, it stores
    //Key/Value pairs relating to the database
    val values = ContentValues()

    //put a key/value entry into the HashMap
    values.put(KEY_TODO_NAME, toDo.toDoName)
    values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)

    // 4
    //update a row
    return db.update(TABLE_NAME, values, "$_ID=?", arrayOf(todoId))
  }

  fun deleteToDo(id: Long) {
    // 1
    val db: SQLiteDatabase = writableDatabase
    // 2
    db.delete(TABLE_NAME, "$_ID=?", arrayOf(id.toString()))
    // 3
    db.close()
  }


  // Functions for emptying and recreating database after testing
  // 1
  fun recreateEmptyDatabase() {
    clearDatabase()
    onCreate(writableDatabase)
  }

  // 2
  fun clearDatabase() {
    writableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
  }

  // 3
  fun getAllText(): String {
    var result = ""
    var cols = arrayOf(KEY_TODO_NAME, KEY_TODO_IS_COMPLETED)
    val cursor = writableDatabase.query(TABLE_NAME, cols,
        null, null, null, null, _ID)
    val COLUMN_NAME = cursor.getColumnIndexOrThrow(KEY_TODO_NAME)
    while (cursor != null && cursor.moveToNext())
    {
      result += "${cursor.getString(COLUMN_NAME)}"
    }
    return result
  }
}