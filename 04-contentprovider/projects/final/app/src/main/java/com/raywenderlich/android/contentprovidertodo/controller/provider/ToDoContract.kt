package com.raywenderlich.android.contentprovidertodo.controller.provider

import android.net.Uri

object ToDoContract {

  // The URI Code for All items
  const val ALL_ITEMS = -2

  //The URI suffix for counting records
  const val COUNT = "count"

  //The URI Authority
  const val AUTHORITY = "com.raywenderlich.android.contentprovidertodo.provider"

  // Only one public table.
  const val CONTENT_PATH = "todoitems"

  // Content URI for this table. Returns all items.
  val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")

  // URI to get the number of entries.
  val ROW_COUNT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_PATH/$COUNT")


  // Single record mime type
  const val SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.raywenderlich" +
      ".contentprovidertodo" +
  ".provider.todoitems"

  // Multiple Record MIME type
  const val MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.item/vnd.com.raywenderlich" +
      ".contentprovidertodo.provider.todoitems"

  // Table Constants
  object ToDoTable {

    // The constants for the table columns
    object Columns {
      const val KEY_TODO_ID: String = "todoid" //The unique ID column
      const val KEY_TODO_NAME: String = "todoname" //The ToDo's Name
      const val KEY_TODO_IS_COMPLETED: String = "iscompleted" //The ToDo's category
    }
  }

}