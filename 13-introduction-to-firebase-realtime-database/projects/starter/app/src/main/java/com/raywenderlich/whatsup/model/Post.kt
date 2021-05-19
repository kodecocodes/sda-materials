package com.raywenderlich.whatsup.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val content: String = "",
    val author: String = "",
    val timestamp: Long = 0L
) : Parcelable {

  fun toMap(): Map<String, Any?> {
    return mapOf(
        "content" to content,
        "author" to author,
        "timestamp" to timestamp
    )
  }
}