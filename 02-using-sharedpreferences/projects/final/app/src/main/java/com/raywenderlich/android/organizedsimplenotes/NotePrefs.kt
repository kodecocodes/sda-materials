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
 *
 */
package com.raywenderlich.organizedsimplenotes

import android.content.SharedPreferences

private const val KEY_APP_BACKGROUND_COLOR = "key_app_background_color"
private const val DEFAULT_COLOR = "Green"
private const val DEFAULT_SORT_ORDER = "FILENAME_ASC"
private const val DEFAULT_PRIORITY_FILTER = "1"
private const val KEY_NOTE_SORT_PREFERENCE = "note_sort_preference"
private const val KEY_NOTE_PRIORITY_SET = "note_priority_set"

class NotePrefs(private val sharedPrefs: SharedPreferences) {

  fun saveNoteSortOrder(noteSortOrder: NoteSortOrder) {
    sharedPrefs.edit()
        .putString(KEY_NOTE_SORT_PREFERENCE, noteSortOrder.name)
        .apply()
  }

  fun getNoteSortOrder() = NoteSortOrder.valueOf(
      sharedPrefs.getString(KEY_NOTE_SORT_PREFERENCE, DEFAULT_SORT_ORDER)
          ?: DEFAULT_SORT_ORDER
  )

  fun saveNotePriorityFilters(priorities: Set<String>) {
    sharedPrefs.edit()
        .putStringSet(KEY_NOTE_PRIORITY_SET, priorities)
        .apply()
  }

  fun getNotePriorityFilters(): Set<String> =
      sharedPrefs.getStringSet(KEY_NOTE_PRIORITY_SET, setOf(DEFAULT_PRIORITY_FILTER))
          ?: setOf(DEFAULT_PRIORITY_FILTER)

  fun saveNoteBackgroundColor(noteBackgroundColor: String) {
    sharedPrefs.edit()
        .putString(KEY_APP_BACKGROUND_COLOR, noteBackgroundColor)
        .apply()
  }

  fun getAppBackgroundColor(): AppBackgroundColor =
      AppBackgroundColor.getColorByName(sharedPrefs.getString(KEY_APP_BACKGROUND_COLOR, DEFAULT_COLOR)
          ?: DEFAULT_COLOR)
}