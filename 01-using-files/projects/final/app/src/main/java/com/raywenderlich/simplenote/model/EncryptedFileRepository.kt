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
import android.util.Log
import java.io.*
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class EncryptedFileRepository(var context: Context) :
    NoteRepository {

  private val passwordString = "Swordfish"

  override fun addNote(note: Note) {
    if (isExternalStorageWritable()) {
      ObjectOutputStream(noteFileOutputStream(note.fileName)).use { output ->
        output.writeObject(encrypt(note.noteText.toByteArray()))
      }
    }
  }

  override fun getNote(fileName: String): Note {
    val note = Note(fileName, "")
    if (isExternalStorageReadable()) {
      ObjectInputStream(noteFileInputStream(note.fileName)).use { stream ->
        val mapFromFile = stream.readObject() as HashMap<String, ByteArray>
        val decrypted = decrypt(mapFromFile)
        if (decrypted != null) {
          note.noteText = String(decrypted)
        }
      }
    }
    return note
  }

  override fun deleteNote(fileName: String): Boolean {
    return isExternalStorageWritable() && noteFile(fileName).delete()
  }

  private fun decrypt(map: HashMap<String, ByteArray>): ByteArray? {
    var decrypted: ByteArray? = null
    try {
      val salt = map["salt"]
      val iv = map["iv"]
      val encrypted = map["encrypted"]

      // 1 regenerate key from password
      val passwordChar = passwordString.toCharArray()
      val pbKeySpec = PBEKeySpec(passwordChar, salt, 1324, 256)
      val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
      val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
      val keySpec = SecretKeySpec(keyBytes, "AES")

      // 2 Decrypt
      val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
      val ivSpec = IvParameterSpec(iv)
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
      decrypted = cipher.doFinal(encrypted)
    } catch (e: Exception) {
      Log.e("SIMPLENOTE", "decryption exception", e)
    }
    // 3
    return decrypted
  }

  private fun encrypt(plainTextBytes: ByteArray): HashMap<String, ByteArray> {
    val map = HashMap<String, ByteArray>()

    try {
      //Random salt for next step
      // 1
      val random = SecureRandom()
      // 2
      val salt = ByteArray(256)
      // 3
      random.nextBytes(salt)

      //PBKDF2 - derive the key from the password, don't use passwords directly
      // 4
      val passwordChar = passwordString.toCharArray() //Turn password into char[] array
      // 5
      val pbKeySpec = PBEKeySpec(passwordChar, salt, 1324, 256) //1324 iterations
      // 6
      val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
      // 7
      val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
      // 8
      val keySpec = SecretKeySpec(keyBytes, "AES")

      //Create initialization vector for AES
      // 9
      val ivRandom = SecureRandom() //not caching previous seeded instance of SecureRandom
      // 10
      val iv = ByteArray(16)
      // 11
      ivRandom.nextBytes(iv)
      // 12
      val ivSpec = IvParameterSpec(iv)

      //Encrypt
      // 13
      val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
      // 14
      val encrypted = cipher.doFinal(plainTextBytes)
      // 15
      map["salt"] = salt
      map["iv"] = iv
      map["encrypted"] = encrypted
    } catch (e: Exception) {
      Log.e("MYAPP", "encryption exception", e)
    }
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