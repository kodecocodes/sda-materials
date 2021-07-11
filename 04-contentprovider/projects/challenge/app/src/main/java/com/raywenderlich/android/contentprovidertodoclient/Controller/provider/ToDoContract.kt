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