package com.raywenderlich.whatsup

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.raywenderlich.whatsup.firebase.realtimeDatabase.COMMENTS_REFERENCE

class WhatsUpApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    FirebaseDatabase.getInstance().apply {
      setPersistenceEnabled(true)
      getReference(COMMENTS_REFERENCE).keepSynced(true)
    }
  }
}