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

package com.raywenderlich.android.simplenote.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.android.simplenote.app.showToast
import com.raywenderlich.android.simplenote.model.EncryptedFileRepository
import com.raywenderlich.android.simplenote.model.ExternalFileRepository
import com.raywenderlich.android.simplenote.model.InternalFileRepository
import com.raywenderlich.android.simplenote.model.Note
import com.raywenderlich.android.simplenote.model.NoteRepository
import com.raywenderlich.android.simplenote.databinding.ActivityMainBinding

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

  private val repo: NoteRepository by lazy { InternalFileRepository(this) }
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnWrite.setOnClickListener {
      // TODO Add code here
    }


    binding.btnRead.setOnClickListener {
      if (binding.edtFileName.text.isNotEmpty()) {
        try {
          val note = repo.getNote(binding.edtFileName.text.toString())
          binding.edtNoteText.setText(note.noteText)
        } catch (e: Exception) {
          showToast("File Read Failed")
        }
      } else {
        showToast("Please provide a Filename")
      }
    }

    binding.btnDelete.setOnClickListener {
      if (binding.edtFileName.text.isNotEmpty()) {
        try {
          if (repo.deleteNote(binding.edtFileName.text.toString())) {
            showToast("File Deleted")
          } else {
            showToast("File Could Not Be Deleted")
          }
        } catch (e: Exception) {
          showToast("File Delete Failed")
        }
        binding.edtFileName.text.clear()
        binding.edtNoteText.text.clear()
      } else {
        showToast("Please provide a Filename")
      }
    }
  }
}
