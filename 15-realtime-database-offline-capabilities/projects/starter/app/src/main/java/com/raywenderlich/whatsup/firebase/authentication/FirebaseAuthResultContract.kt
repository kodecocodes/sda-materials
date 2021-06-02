package com.raywenderlich.whatsup.firebase.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.raywenderlich.whatsup.R

class FirebaseAuthResultContract : ActivityResultContract<Int, IdpResponse>() {

    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    override fun createIntent(context: Context, input: Int?): Intent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setIsSmartLockEnabled(false)
        .build()

    override fun parseResult(resultCode: Int, intent: Intent?): IdpResponse? = when (resultCode) {
        Activity.RESULT_OK -> IdpResponse.fromResultIntent(intent)
        else -> null
    }
}