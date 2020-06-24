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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.raywenderlich.droidquiz.R
import com.raywenderlich.droidquiz.data.model.QuestionAndAllAnswers
import com.raywenderlich.droidquiz.viewmodel.QuestionViewModel
import kotlinx.android.synthetic.main.activity_question.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.newTask

class QuestionActivity : AppCompatActivity() {

  private lateinit var viewModel: QuestionViewModel
  private var answer: String = ""

  companion object {
    const val SCORE = "SCORE"
    const val NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTIONS"
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_question)
    viewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)
    getQuestionsAndAnswers()
  }

  private fun displayProgressBar() {
    progressBar.visibility = View.VISIBLE
  }

  private fun hideProgressBar() {
    progressBar.visibility = View.GONE
  }

  private fun displayEmptyView() {
    emptyDroid.visibility = View.VISIBLE
    emptyTextView.visibility = View.VISIBLE
  }

  private fun displayQuestionsView() {
    questionsRadioGroup.visibility = View.VISIBLE
    questionTextView.visibility = View.VISIBLE
    button.visibility = View.VISIBLE
  }

  private fun goToResultActivity(numberOfQuestions: Int) {
    startActivity(intentFor<ResultActivity>(
      SCORE to viewModel.getScore(),
      NUMBER_OF_QUESTIONS to numberOfQuestions
    ).newTask().clearTask())
  }

  private fun saveScore() {
    val radioButton = findViewById<RadioButton>(questionsRadioGroup.checkedRadioButtonId)
    radioButton?.let {
      if (it.text == answer) {
        viewModel.increaseScore()
        longToast("Correct!")
      } else {
        longToast("Wrong!")
      }
    }
  }

  private fun updateUI(currentQuestion: Int, questionAndAllAnswers: List<QuestionAndAllAnswers>) {//1
    questionsRadioGroup.clearCheck()
    questionTextView.text = questionAndAllAnswers[currentQuestion].question?.text
    option1.text = questionAndAllAnswers[currentQuestion].answers[0].text
    option2.text = questionAndAllAnswers[currentQuestion].answers[1].text
    option3.text = questionAndAllAnswers[currentQuestion].answers[2].text
  }


  private fun getCorrectAnswer(currentQuestion: Int, questionAndAllAnswers: List<QuestionAndAllAnswers>): String {//2
    for (answer in questionAndAllAnswers[currentQuestion].answers) {
      if (answer.isCorrect) {
        this.answer = answer.text
        return answer.text
      }
    }
    return ""
  }

  fun nextQuestion(view: View) {
    saveScore()
    viewModel.nextQuestion()
  }

  private fun displayCurrentQuestion(questionAndAllAnswers: List<QuestionAndAllAnswers>) {
    viewModel.getCurrentQuestion().observe(this, Observer { currentQuestion ->
      if (currentQuestion == questionAndAllAnswers.size) {
        goToResultActivity(questionAndAllAnswers.size)
      } else {
        updateUI(currentQuestion, questionAndAllAnswers)
        getCorrectAnswer(currentQuestion, questionAndAllAnswers)
      }
    })
  }

  private fun getQuestionsAndAnswers() {
    displayProgressBar()
    viewModel.getQuestionsAndSavedAnswers().observe(this, Observer {
      hideProgressBar()
      if (it.isEmpty()) {
        displayEmptyView()
      } else {
        displayQuestionsView()
        displayCurrentQuestion(it)
      }
    })
  }
}
