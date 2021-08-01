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

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.raywenderlich.android.organizedsimplenotes.databinding.NotePopupBinding


class NoteDialogFragment : DialogFragment() {

  private lateinit var listener: NoticeNoteDialogListener
  private lateinit var notePopupBinding: NotePopupBinding

  interface NoticeNoteDialogListener {
    fun onNoteDialogPositiveClick(note: Note, isEdited: Boolean)
    fun onNoteDialogNeutralClick(note: Note)
  }

  companion object {
    private const val KEY_NOTE = "note"
    fun newInstance(note: Note? = null, listener: NoticeNoteDialogListener) = NoteDialogFragment().apply {
      arguments = Bundle().apply {
        putSerializable(KEY_NOTE, note)
      }

      this.listener = listener
    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val note = arguments?.getSerializable(KEY_NOTE) as? Note ?: Note()

    val builder = AlertDialog.Builder(requireActivity())
    notePopupBinding = NotePopupBinding.inflate(LayoutInflater.from(context))

    builder.setView(notePopupBinding.root)

    val isExistingNote = note.fileName.isNotBlank()

    notePopupBinding.edtFileName.isEnabled = !isExistingNote
    notePopupBinding.edtFileName.setText(note.fileName)
    notePopupBinding.edtNoteText.setText(note.noteText)
    notePopupBinding.notePriority.setSelection(if (note.priority == 4) 0 else note.priority)

    builder.setTitle(getString(if (isExistingNote) R.string.edit_note_title else R.string.add_note_title))

    if (isExistingNote) {
      builder.setNeutralButton(R.string.text_delete) { _, _ -> listener.onNoteDialogNeutralClick(note) }
    }

    notePopupBinding.notePriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {}

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        note.priority = if (position <= 0) 4 else position
      }
    }

    builder.setPositiveButton(R.string.text_save) { _, _ ->
      note.noteText = notePopupBinding.edtNoteText.text.toString()
      note.fileName = notePopupBinding.edtFileName.text.toString()
      listener.onNoteDialogPositiveClick(note, isExistingNote)
    }.setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }

    return builder.create()
  }

}