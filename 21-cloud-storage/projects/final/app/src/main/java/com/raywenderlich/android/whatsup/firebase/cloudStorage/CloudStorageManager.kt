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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.whatsup.firebase.cloudStorage

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

private const val PHOTOS_REFERENCE = "photos"

class CloudStorageManager {

  private val firebaseStorage by lazy { FirebaseStorage.getInstance() }

  fun uploadPhoto(selectedImageUri: Uri, onSuccessAction: (String) -> Unit) {
    //1
    val photosReference = firebaseStorage.getReference(PHOTOS_REFERENCE)

    //2
    selectedImageUri.lastPathSegment?.let { segment ->
      //3
      val photoReference = photosReference.child(segment)

      //4
      photoReference.putFile(selectedImageUri)
          //5
          .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            val exception = task.exception

            if (!task.isSuccessful && exception != null) {
              throw exception
            }
            return@Continuation photoReference.downloadUrl
          })
          //6
          .addOnCompleteListener { task ->
            if (task.isSuccessful) {
              val downloadUri = task.result
              onSuccessAction(downloadUri.toString())
            }
          }
    }
  }
}