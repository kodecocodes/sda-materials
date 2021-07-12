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

package com.raywenderlich.android.contentprovidertodoclient.controller.provider

import android.net.Uri

object ToDoContract {

  const val ALL_ITEMS = -2
  const val COUNT = "count"

  const val AUTHORITY = "com.raywenderlich.contentprovidertodo.provider"

  // Only one public table.
  const val CONTENT_PATH = "todoitems"

  // Content URI for this table. Returns all items.
  val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")

  // URI to get the number of entries.
  val ROW_COUNT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_PATH/$COUNT")

  const val SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.raywenderlich.contentprovidertodo" +
      ".provider.todoitems"

  const val MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.item/vnd.com.raywenderlich" +
      ".contentprovidertodo" +
      ".provider.todoitems"

  object ToDoTable {

    object Columns {
      // 4
      const val KEY_TODO_ID: String = "todoid" //The unique ID column
      // 5
      const val KEY_TODO_NAME: String = "todoname" //The ToDo's Name
      // 6
      const val KEY_TODO_IS_COMPLETED: String = "iscompleted" //The ToDo's category
    }
  }

}