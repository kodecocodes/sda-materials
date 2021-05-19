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

package com.raywenderlich.droidquiz.view

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEachIndexed
import androidx.lifecycle.Observer
import com.raywenderlich.droidquiz.R
import com.raywenderlich.droidquiz.data.Repository
import com.raywenderlich.droidquiz.data.model.QuizState
import com.raywenderlich.droidquiz.getViewModel
import com.raywenderlich.droidquiz.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.activity_question.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class QuestionActivity : AppCompatActivity() {

  companion object {
    const val SCORE = "SCORE"
    const val NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTIONS"
  }

  private val viewModel by lazy { getViewModel { QuizViewModel(Repository()) } }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_question)
    getQuestionsAndAnswers()
  }

  private fun getQuestionsAndAnswers() {
    viewModel.getCurrentState().observe(this, Observer {
      render(it)
    })
  }

  private fun render(state: QuizState) {
    when (state) {
      is QuizState.Empty -> renderEmptyState()
      is QuizState.Data -> renderDataState(state)
      is QuizState.Finish -> goToResultActivity(state.numberOfQuestions, state.score)
      is QuizState.Loading -> renderLoadingState()
    }
  }

  private fun renderDataState(quizState: QuizState.Data) {
    progressBar.visibility = View.GONE
    displayQuestionsView()
    questionsRadioGroup.clearCheck()
    questionTextView.text = quizState.data.question?.text
    questionsRadioGroup.forEachIndexed { index, view ->
      if (index < quizState.data.answers.size)
        (view as RadioButton).text = quizState.data.answers[index].text
    }
  }

  private fun renderLoadingState() {
    progressBar.visibility = View.VISIBLE
  }

  private fun renderEmptyState() {
    progressBar.visibility = View.GONE
    emptyDroid.visibility = View.VISIBLE
    emptyTextView.visibility = View.VISIBLE
  }

  fun nextQuestion(view: View) {
    val radioButton = findViewById<RadioButton>(questionsRadioGroup.checkedRadioButtonId)
    val selectedOption = questionsRadioGroup.indexOfChild(radioButton)
    viewModel.nextQuestion(selectedOption)
  }

  private fun displayQuestionsView() {
    questionsRadioGroup.visibility = View.VISIBLE
    questionTextView.visibility = View.VISIBLE
    button.visibility = View.VISIBLE
  }

  private fun goToResultActivity(numberOfQuestions: Int, score: Int) {
    startActivity(
      intentFor<ResultActivity>(
        SCORE to score,
        NUMBER_OF_QUESTIONS to numberOfQuestions
      ).newTask().clearTask()
    )
  }
}
