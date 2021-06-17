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

package com.raywenderlich.whatsup.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.IdpResponse
import com.raywenderlich.whatsup.R
import com.raywenderlich.whatsup.databinding.ActivityLoginBinding
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.firebase.authentication.FirebaseAuthResultContract
import com.raywenderlich.whatsup.firebase.authentication.RC_SIGN_IN
import com.raywenderlich.whatsup.ui.Router
import com.raywenderlich.whatsup.util.showToast

class LoginActivity : AppCompatActivity() {

  private val router by lazy { Router() }
  private val authenticationManager by lazy { AuthenticationManager() }
  private lateinit var binding: ActivityLoginBinding

  companion object {
    fun createIntent(context: Context) = Intent(context, LoginActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)
    initialize()
  }

  private fun initialize() {
    setSupportActionBar(binding.loginToolbar)
    continueToHomeScreenIfUserSignedIn()
    setupClickListeners()
  }

  private fun continueToHomeScreenIfUserSignedIn() = if (isUserSignedIn()) router.startHomeScreen(this) else Unit

  private fun setupClickListeners() {
    binding.googleSignInButton.setOnClickListener { firebaseAuthResultLauncher.launch(RC_SIGN_IN) }
  }

  private fun isUserSignedIn() = authenticationManager.isUserSignedIn()

  private val firebaseAuthResultLauncher =
      registerForActivityResult(FirebaseAuthResultContract()) { idpResponse ->
        handleFirebaseAuthResponse(idpResponse)
      }

  private fun handleFirebaseAuthResponse(idpResponse: IdpResponse?) {
    when {
      (idpResponse == null || idpResponse.error != null) -> {
        showToast(getString(R.string.sign_in_failed))
      }
      else -> {
        router.startHomeScreen(this)
      }
    }
  }
}
