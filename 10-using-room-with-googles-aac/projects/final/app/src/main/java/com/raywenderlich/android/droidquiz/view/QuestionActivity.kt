/*
 *
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

package com.raywenderlich.android.droidquiz.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEachIndexed
import com.raywenderlich.android.droidquiz.R
import com.raywenderlich.android.droidquiz.data.Repository
import com.raywenderlich.android.droidquiz.data.model.QuizState
import com.raywenderlich.android.droidquiz.databinding.ActivityQuestionBinding
import com.raywenderlich.android.droidquiz.getViewModel
import com.raywenderlich.android.droidquiz.viewmodel.QuizViewModel

class QuestionActivity : AppCompatActivity() {

  private lateinit var binding: ActivityQuestionBinding

  companion object {
    const val SCORE = "SCORE"
    const val NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTIONS"
  }

  private val viewModel by lazy { getViewModel { QuizViewModel(Repository()) } }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityQuestionBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.button.setOnClickListener { nextQuestion() }
    getQuestionsAndAnswers()
  }

  private fun getQuestionsAndAnswers() {
    viewModel.getCurrentState().observe(this) {
      render(it)
    }
  }

  private fun render(state: QuizState) {
    when (state) {
      is QuizState.EmptyState -> renderEmptyState()
      is QuizState.DataState -> renderDataState(state)
      is QuizState.FinishState -> goToResultActivity(state.numberOfQuestions, state.score)
      is QuizState.LoadingState -> renderLoadingState()
    }
  }

  private fun renderDataState(quizState: QuizState.DataState) {
    binding.progressBar.visibility = View.GONE
    displayQuestionsView()
    binding.questionsRadioGroup.clearCheck()
    binding.questionTextView.text = quizState.data.question?.text
    binding.questionsRadioGroup.forEachIndexed { index, view ->
      if (index < quizState.data.answers.size)
        (view as RadioButton).text = quizState.data.answers[index].text
    }
  }

  private fun renderLoadingState() {
    binding.progressBar.visibility = View.VISIBLE
  }

  private fun renderEmptyState() {
    binding.progressBar.visibility = View.GONE
    binding.emptyDroid.visibility = View.VISIBLE
    binding.emptyTextView.visibility = View.VISIBLE
  }

  private fun nextQuestion() {
    val radioButton = findViewById<RadioButton>(binding.questionsRadioGroup.checkedRadioButtonId)
    val selectedOption = binding.questionsRadioGroup.indexOfChild(radioButton)
    if (selectedOption != -1) {
      viewModel.nextQuestion(selectedOption)
    } else {
      Toast.makeText(this, getString(R.string.please_select_an_option), Toast.LENGTH_SHORT).show()
    }
  }

  private fun displayQuestionsView() {
    binding.questionsRadioGroup.visibility = View.VISIBLE
    binding.questionTextView.visibility = View.VISIBLE
    binding.button.visibility = View.VISIBLE
  }

  private fun goToResultActivity(numberOfQuestions: Int, score: Int) {
    val intent = Intent(this, ResultActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
      putExtra(SCORE, score)
      putExtra(NUMBER_OF_QUESTIONS, numberOfQuestions)
    }

    startActivity(intent)
  }
}
