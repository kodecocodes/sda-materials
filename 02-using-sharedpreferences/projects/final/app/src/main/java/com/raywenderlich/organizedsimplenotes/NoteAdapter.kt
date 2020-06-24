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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*
import java.text.SimpleDateFormat
import java.util.*

private const val NO_NOTE_PRIORITY = "None"

class NoteAdapter(
    private val context: Context,
    private var priorities: Set<String>,
    private var order: NoteSortOrder,
    private val onNoteClicked: (Note) -> Unit) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

  private val repo: NoteRepository by lazy { InternalFileRepository(context) }
  private val simpleDateFormat by lazy { SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.getDefault()) }

  private val notes: MutableList<Note> = mutableListOf()

  init {
    loadNotes()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
    return ViewHolder(view)
  }

  override fun getItemCount(): Int = notes.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindViews(notes[position], onNoteClicked)
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViews(note: Note, onNoteClicked: (Note) -> Unit) {
      val priorityText = if (note.priority != 4) note.priority.toString() else NO_NOTE_PRIORITY

      itemView.txtFileName.text = note.fileName
      itemView.txtNoteText.text = note.noteText
      itemView.txtPriority.text = priorityText
      itemView.txtLastModified.text = simpleDateFormat.format(note.dateModified)
      itemView.setOnClickListener { onNoteClicked(note) }
    }
  }

  fun addNote(note: Note): Boolean {
    val isNoteAdded = repo.addNote(note)

    if (isNoteAdded) {
      loadNotes()
    }

    return isNoteAdded
  }

  fun editNote(note: Note) {
    repo.editNote(note)
    loadNotes()
  }

  fun deleteNote(fileName: String) {
    repo.deleteNote(fileName)
    loadNotes()
  }

  fun updateNotesFilters(priorities: Set<String>? = null, order: NoteSortOrder? = null) {
    if (priorities != null) {
      this.priorities = priorities
    }

    if (order != null) {
      this.order = order
    }

    loadNotes()
  }

  private fun loadNotes() {
    notes.clear()
    notes.addAll(getNotes())
    notifyDataSetChanged()
  }

  private fun getNotes() = repo.getNotesWithPrioritySortedBy(priorities, order)
}