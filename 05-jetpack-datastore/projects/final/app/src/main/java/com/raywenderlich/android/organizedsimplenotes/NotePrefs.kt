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
package com.raywenderlich.android.organizedsimplenotes

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

private const val DEFAULT_COLOR = "Green"
private const val DEFAULT_SORT_ORDER = "FILENAME_ASC"
private const val DEFAULT_PRIORITY_FILTER = "1"

class NotePrefs(private val dataStore: DataStore<Preferences>) {

  companion object {
    const val PREFS_NAME = "user_preferences"
    private val BACKGROUND_COLOR = stringPreferencesKey("key_app_background_color")
    private val NOTE_SORT_ORDER = stringPreferencesKey("note_sort_preference")
    private val NOTE_PRIORITY_SET = stringSetPreferencesKey("note_priority_set")
  }

  suspend fun saveNoteSortOrder(noteSortOrder: NoteSortOrder) {
    dataStore.edit { preferences ->
      preferences[NOTE_SORT_ORDER] = noteSortOrder.name
    }
  }

  fun getNoteSortOrder() = runBlocking {
    NoteSortOrder.valueOf(dataStore.data.first()[NOTE_SORT_ORDER] ?: DEFAULT_SORT_ORDER)
  }

  suspend fun saveNotePriorityFilters(priorities: Set<String>) {
    dataStore.edit { preferences ->
      preferences[NOTE_PRIORITY_SET] = priorities
    }
  }

  fun getNotePriorityFilters() = runBlocking {
    dataStore.data.first()[NOTE_PRIORITY_SET] ?: setOf(DEFAULT_PRIORITY_FILTER)
  }

  suspend fun saveNoteBackgroundColor(noteBackgroundColor: String) {
    dataStore.edit { preferences ->
      preferences[BACKGROUND_COLOR] = noteBackgroundColor
    }
  }

  fun getAppBackgroundColor(): AppBackgroundColor =
    runBlocking {
      AppBackgroundColor.getColorByName(dataStore.data.first()[BACKGROUND_COLOR] ?: DEFAULT_COLOR)
    }

  val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }.map { preferences ->
      val backgroundColor =  AppBackgroundColor.getColorByName(preferences[BACKGROUND_COLOR] ?: DEFAULT_COLOR)

      UserPreferences(backgroundColor)
    }

}