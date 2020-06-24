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

package com.raywenderlich.organizedsimplenotes

import android.content.Context
import java.io.File
import java.util.*

class InternalFileRepository(private var context: Context) : NoteRepository {

  private val allNotes by lazy { getNotes() }

  override fun editNote(note: Note) {
    note.noteText = note.toString()
    context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use { output ->
      output.write(note.noteText.toByteArray())
    }
  }

  override fun addNote(note: Note): Boolean {
    val text = note.toString()

    if (!noteFile(note.fileName).exists()) {
      context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use { output ->
        output.write(text.toByteArray())
      }
      allNotes.add(note)
      return true
    }
    return false
  }

  override fun getNote(fileName: String): Note {
    val note = Note(fileName, "", 0)
    context.openFileInput(fileName).use { stream ->
      val text = stream.bufferedReader().use {
        it.readText()
      }
      note.dateModified = Date(noteFile(fileName).lastModified())
      note.priority = text.takeLast(1).toInt()
      note.noteText = text.dropLast(1)
    }
    return note
  }

  override fun deleteNote(fileName: String): Boolean {
    allNotes.removeIf { it.fileName == fileName }
    return noteFile(fileName).delete()
  }

  override fun getNotes(): MutableList<Note> {
    return context.filesDir
        .listFiles()
        .map { getNote(it.name) }.toMutableList()
  }

  override fun getNotesWithPrioritySortedBy(
      priorities: Set<String>,
      order: NoteSortOrder): List<Note> {
    val filteredNotes: List<Note> = if (priorities.count() == 0) {
      allNotes
    } else {
      allNotes.filter { priorities.contains(it.priority.toString()) }
    }

    return when (order) {
      NoteSortOrder.FILENAME_ASC -> filteredNotes.sortedBy { it.fileName }
      NoteSortOrder.FILENAME_DESC -> filteredNotes.sortedByDescending { it.fileName }
      NoteSortOrder.DATE_LAST_MOD_ASC -> filteredNotes.sortedBy { it.dateModified }
      NoteSortOrder.DATE_LAST_MOD_DESC -> filteredNotes.sortedByDescending { it.dateModified }
      NoteSortOrder.PRIORITY_ASC -> filteredNotes.sortedBy { it.priority }
      else -> filteredNotes.sortedByDescending { it.priority }
    }
  }

  private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)

  private fun noteDirectory(): String = context.filesDir.absolutePath
}