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

package com.raywenderlich.droidquiz.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.droidquiz.data.QuestionRepository
import com.raywenderlich.droidquiz.data.Repository
import com.raywenderlich.droidquiz.data.model.QuestionAndAllAnswers

class QuestionViewModel(private val repository: QuestionRepository = Repository()) : ViewModel() {
  private val allQuestionsAndAnswers = MediatorLiveData<List<QuestionAndAllAnswers>>()//1
  private var currentQuestion = MediatorLiveData<Int>()//2
  private var score = 0//3

  init {
    getAllQuestions()
    currentQuestion.postValue(0)
  }

  private fun getAllQuestions() {
    allQuestionsAndAnswers.addSource(repository.getQuestionAndAllAnswers()) {
      allQuestionsAndAnswers.postValue(it)
    }
  }

  fun getCurrentQuestion() = currentQuestion //1

  fun getScore() = score //2

  fun getQuestionsAndSavedAnswers() = allQuestionsAndAnswers //3

  fun increaseScore() = score++  //4

  fun nextQuestion(){ //5
    val currentValue = currentQuestion.value?.plus(1)
    currentQuestion.postValue(currentValue)
  }

}