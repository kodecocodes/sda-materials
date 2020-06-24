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

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import com.raywenderlich.organizedsimplenotes.NoteSortOrder.*

private const val PRIORITY_ONE = "1"
private const val PRIORITY_TWO = "2"
private const val PRIORITY_THREE = "3"

class MainActivity : AppCompatActivity(), NoteDialogFragment.NoticeNoteDialogListener {

  private val notePrefs: NotePrefs by lazy {
    NotePrefs(PreferenceManager.getDefaultSharedPreferences(this))
  }

  private val noteAdapter: NoteAdapter by lazy { NoteAdapter(this, priorities, FILENAME_ASC, ::showEditNoteDialog) }
  private val priorities: MutableSet<String> = mutableSetOf()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = noteAdapter
    fab.setOnClickListener { showAddNoteDialog() }

    changeNotesBackgroundColor()
  }

  private fun showAddNoteDialog() {
    val newFragment = NoteDialogFragment.newInstance(listener = this)
    newFragment.show(supportFragmentManager, "notes")
  }

  private fun showEditNoteDialog(note: Note) {
    val newFragment = NoteDialogFragment.newInstance(note, this)
    newFragment.show(supportFragmentManager, "notes")
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.menu_note, menu)

    when (notePrefs.getNoteSortOrder()) {
      FILENAME_ASC -> menu.findItem(R.id.sort_by_filename_asc).isChecked = true
      FILENAME_DESC -> menu.findItem(R.id.sort_by_filename_asc).isChecked = true
      DATE_LAST_MOD_ASC -> menu.findItem(R.id.sort_by_date_last_modified_asc).isChecked = true
      DATE_LAST_MOD_DESC -> menu.findItem(R.id.sort_by_date_last_modified_desc).isChecked = true
      PRIORITY_ASC -> menu.findItem(R.id.sort_by_priority_asc).isChecked = true
      PRIORITY_DESC -> menu.findItem(R.id.sort_by_priority_desc).isChecked = true
    }

    menu.findItem(R.id.priority_1).isChecked = priorities.contains(PRIORITY_ONE)
    menu.findItem(R.id.priority_2).isChecked = priorities.contains(PRIORITY_TWO)
    menu.findItem(R.id.priority_3).isChecked = priorities.contains(PRIORITY_THREE)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    return when (item.itemId) {
      // Background color preference selected
      R.id.change_note_background_color -> {
        showNoteBackgroundColorDialog()
        true
      }
      R.id.sort_by_date_last_modified_asc -> {
        item.isChecked = true
        updateNoteSortOrder(DATE_LAST_MOD_ASC)
        true
      }
      R.id.sort_by_date_last_modified_desc -> {
        item.isChecked = true
        updateNoteSortOrder(DATE_LAST_MOD_DESC)
        true
      }
      R.id.sort_by_filename_asc -> {
        item.isChecked = true
        updateNoteSortOrder(FILENAME_ASC)
        true
      }
      R.id.sort_by_filename_desc -> {
        item.isChecked = true
        updateNoteSortOrder(FILENAME_DESC)
        true
      }
      R.id.sort_by_priority_asc -> {
        item.isChecked = true
        updateNoteSortOrder(PRIORITY_ASC)
        true
      }
      R.id.sort_by_priority_desc -> {
        item.isChecked = true
        updateNoteSortOrder(PRIORITY_DESC)
        true
      }
      R.id.priority_1 -> {
        item.isChecked = !item.isChecked
        togglePriorityState(PRIORITY_ONE, item.isChecked)

        updateNotePrioritiesFilter(priorities)
        true
      }
      R.id.priority_2 -> {
        item.isChecked = !item.isChecked
        togglePriorityState(PRIORITY_TWO, item.isChecked)
        true
      }
      R.id.priority_3 -> {
        item.isChecked = !item.isChecked
        togglePriorityState(PRIORITY_THREE, item.isChecked)
        true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun togglePriorityState(priority: String, isActive: Boolean) {
    if (isActive) {
      priorities.add(priority)
    } else {
      priorities.remove(priority)
    }
    updateNotePrioritiesFilter(priorities)
  }

  private fun showNoteBackgroundColorDialog() {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_radio_group)

    val radioGroup = dialog.findViewById(R.id.radio_group) as RadioGroup
    val greenButton = dialog.findViewById<RadioButton>(R.id.greenOption)
    val orangeButton = dialog.findViewById<RadioButton>(R.id.orangeOption)
    val purpleButton = dialog.findViewById<RadioButton>(R.id.purpleOption)

    val radioButtons = setOf(greenButton, orangeButton, purpleButton)

    val currentBackgroundColor = getCurrentBackgroundColorString()
    greenButton.isChecked = greenButton.text == currentBackgroundColor
    orangeButton.isChecked = orangeButton.text == currentBackgroundColor
    purpleButton.isChecked = purpleButton.text == currentBackgroundColor

    radioGroup.setOnCheckedChangeListener { _, checkedId ->
      val selectedRadioButton = radioButtons.firstOrNull { it.id == checkedId }

      if (selectedRadioButton != null) {
        notePrefs.saveNoteBackgroundColor(selectedRadioButton.text.toString())
        changeNotesBackgroundColor()
      }
    }
    dialog.show()
  }

  private fun changeNotesBackgroundColor() = window.decorView.setBackgroundResource(getCurrentBackgroundColorInt())

  private fun getCurrentBackgroundColorString(): String = notePrefs.getAppBackgroundColor().displayString

  private fun getCurrentBackgroundColorInt(): Int = notePrefs.getAppBackgroundColor().intColor

  override fun onNoteDialogNeutralClick(note: Note) = noteAdapter.deleteNote(note.fileName)

  override fun onNoteDialogPositiveClick(note: Note, isEdited: Boolean) {
    if (isEdited) {
      noteAdapter.editNote(note)
    } else {
      if (noteAdapter.addNote(note)) {
        showToast("Note Was Added Successfully!")
      } else {
        showToast("That File Already Exists.")
      }
    }
  }

  private fun updateNoteSortOrder(sortOrder: NoteSortOrder) {
    noteAdapter.updateNotesFilters(order = sortOrder)
    // TODO: Save the sort order to prefs
  }

  private fun updateNotePrioritiesFilter(priorities: Set<String>) {
    noteAdapter.updateNotesFilters(priorities = priorities)
    // TODO: Save the priorities to prefs
  }
}
