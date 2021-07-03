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

package com.raywenderlich.android.droidquiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.android.droidquiz.data.QuizRepository
import com.raywenderlich.android.droidquiz.data.model.QuestionAndAllAnswers
import com.raywenderlich.android.droidquiz.data.model.QuizState

class QuizViewModel(repository: QuizRepository) : ViewModel() {

  private val questionAndAnswers = MediatorLiveData<QuestionAndAllAnswers>()
  private val currentQuestion = MutableLiveData<Int>()
  private val currentState = MediatorLiveData<QuizState>()
  private val allQuestionAndAllAnswers = repository.getQuestionAndAllAnswers()
  private var score: Int = 0

  init {
    currentState.postValue(QuizState.LoadingState)
    addStateSources()
    addQuestionSources()
    currentQuestion.postValue(0)
  }

  fun getCurrentState(): LiveData<QuizState> = currentState

  private fun addQuestionSources() {
    questionAndAnswers.addSource(currentQuestion) { currentQuestionNumber ->
      val questions = allQuestionAndAllAnswers.value

      if (questions != null && currentQuestionNumber < questions.size) {
        questionAndAnswers.postValue(questions[currentQuestionNumber])
      }
    }

    questionAndAnswers.addSource(allQuestionAndAllAnswers) { questionsAndAnswers ->
      val currentQuestionNumber = currentQuestion.value

      if (currentQuestionNumber != null && questionsAndAnswers.isNotEmpty()) {
        questionAndAnswers.postValue(questionsAndAnswers[currentQuestionNumber])
      }
    }
  }

  fun nextQuestion(choice: Int) { // 1
    verifyAnswer(choice)
    changeCurrentQuestion()
  }

  private fun verifyAnswer(choice: Int) { // 2
    val currentQuestion = questionAndAnswers.value

    if (currentQuestion != null && currentQuestion.answers[choice].isCorrect) {
      score++
    }
  }

  private fun changeCurrentQuestion() {
    currentQuestion.postValue(currentQuestion.value?.plus(1))
  }

  private fun addStateSources() {
    currentState.addSource(currentQuestion) { currentQuestionNumber ->
      if (currentQuestionNumber == allQuestionAndAllAnswers.value?.size) {
        currentState.postValue(QuizState.FinishState(currentQuestionNumber, score))
      }
    }
    currentState.addSource(allQuestionAndAllAnswers) { allQuestionsAndAnswers ->
      if (allQuestionsAndAnswers.isEmpty()) {
        currentState.postValue(QuizState.EmptyState)
      }
    }
    currentState.addSource(questionAndAnswers) { questionAndAnswers ->
      currentState.postValue(QuizState.DataState(questionAndAnswers))
    }
  }
}