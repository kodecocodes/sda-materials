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
package com.raywenderlich.android.organizedsimplenotes

import android.content.SharedPreferences

private const val KEY_APP_BACKGROUND_COLOR = "key_app_background_color"
private const val DEFAULT_COLOR = "Green"
private const val DEFAULT_SORT_ORDER = "FILENAME_ASC"
private const val DEFAULT_PRIORITY_FILTER = "1"

//TODO: Add some constants for priorities and sort order

class NotePrefs(private val sharedPrefs: SharedPreferences) {

  fun saveNoteSortOrder(noteSortOrder: NoteSortOrder) {
    //TODO: Add the code to save the sort order to sharedprefs
  }

  //TODO: Add the code to read the sort order to sharedprefs instead
  fun getNoteSortOrder() = NoteSortOrder.FILENAME_ASC

  fun saveNotePriorityFilters(priorities: Set<String>) {
    //TODO: Add the code to save the priorities to sharedprefs
  }

  fun getNotePriorityFilters(): Set<String> = setOf()
  //TODO: Add the code to read the priorities to sharedprefs instead

  fun saveNoteBackgroundColor(noteBackgroundColor: String) {
    sharedPrefs.edit()
        .putString(KEY_APP_BACKGROUND_COLOR, noteBackgroundColor)
        .apply()
  }

  fun getAppBackgroundColor(): AppBackgroundColor =
      AppBackgroundColor.getColorByName(sharedPrefs.getString(KEY_APP_BACKGROUND_COLOR, DEFAULT_COLOR)
          ?: DEFAULT_COLOR)
}