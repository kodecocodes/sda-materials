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

package com.raywenderlich.simplenote.model

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class EncryptedFileRepository(var context: Context) :
    NoteRepository {

  private val passwordString = "Swordfish"

  override fun addNote(note: Note) {
    // TODO add code from the tutorial here
  }

  override fun getNote(fileName: String): Note {
    // TODO remove the following return statement
    //  and add code from the tutorial here instead.
    return Note("", "")
  }

  override fun deleteNote(fileName: String): Boolean {
    if (isExternalStorageWritable()) {
      return noteFile(fileName).delete()
    }
    return false
  }

  private fun decrypt(map: HashMap<String, ByteArray>): ByteArray? {
    // TODO remove the following two lines of code
    //  and add code from the tutorial here instead.
    var decrypted: ByteArray? = null
    return decrypted
  }

  private fun encrypt(plainTextBytes: ByteArray): HashMap<String, ByteArray> {
    // TODO remove the following two lines of code
    //  and add code from the tutorial here instead.
    val map = HashMap<String, ByteArray>()
    return map
  }

  private fun noteDirectory(): File? = context.getExternalFilesDir(null)

  private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)

  private fun noteFileOutputStream(fileName: String): FileOutputStream = FileOutputStream(noteFile(fileName))

  private fun noteFileInputStream(fileName: String): FileInputStream =
      FileInputStream(noteFile(fileName))

  fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
  }

  fun isExternalStorageReadable(): Boolean {
    return Environment.getExternalStorageState() in
        setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
  }

}