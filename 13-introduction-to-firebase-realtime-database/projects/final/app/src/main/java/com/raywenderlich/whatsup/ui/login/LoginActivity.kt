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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.whatsup.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.raywenderlich.whatsup.R
import com.raywenderlich.whatsup.firebase.authentication.AuthenticationManager
import com.raywenderlich.whatsup.firebase.authentication.RC_SIGN_IN
import com.raywenderlich.whatsup.ui.Router
import com.raywenderlich.whatsup.util.showToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

  private val router by lazy { Router() }
  private val authenticationManager by lazy { AuthenticationManager() }

  companion object {
    fun createIntent(context: Context) = Intent(context, LoginActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    initialize()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == RC_SIGN_IN) {

      if (resultCode == Activity.RESULT_OK) {
        router.startHomeScreen(this)
      } else {
        showToast(getString(R.string.sign_in_failed))
      }
    }
  }

  private fun initialize() {
    setSupportActionBar(loginToolbar)
    continueToHomeScreenIfUserSignedIn()
    setupClickListeners()
  }

  private fun continueToHomeScreenIfUserSignedIn() = if (isUserSignedIn()) router.startHomeScreen(this) else Unit

  private fun setupClickListeners() {
    googleSignInButton.setOnClickListener { authenticationManager.startSignInFlow(this) }
  }

  private fun isUserSignedIn() = authenticationManager.isUserSignedIn()
}
