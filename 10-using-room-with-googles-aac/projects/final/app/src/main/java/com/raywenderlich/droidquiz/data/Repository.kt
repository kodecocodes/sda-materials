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

package com.raywenderlich.droidquiz.data

import android.os.AsyncTask
import com.raywenderlich.droidquiz.QuizApplication
import com.raywenderlich.droidquiz.data.db.QuizDao
import com.raywenderlich.droidquiz.data.model.Answer
import com.raywenderlich.droidquiz.data.model.Question

class Repository : QuizRepository {

  private val quizDao: QuizDao by lazy { QuizApplication.database.questionsDao() }
  private val allQuestions by lazy { quizDao.getAllQuestions() }
  private val allQuestionsAndAllAnswers by lazy { quizDao.getQuestionAndAllAnswers() }

  override fun getSavedQuestions() = allQuestions

  override fun getQuestionAndAllAnswers() = allQuestionsAndAllAnswers

  override fun saveQuestion(question: Question) {
    AsyncTask.execute { quizDao.insert(question) }
  }

  override fun saveAnswer(answer: Answer) {
    AsyncTask.execute { quizDao.insert(answer) }
  }

  override fun deleteQuestions() {
    AsyncTask.execute { quizDao.clearQuestions() }
  }
}