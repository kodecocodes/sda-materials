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

import com.raywenderlich.droidquiz.data.model.Answer
import com.raywenderlich.droidquiz.data.model.Question

object QuestionInfoProvider {

  var questionList = initQuestionList()
  var answerList = initAnswersList()

  private fun initQuestionList(): MutableList<Question> {
    val questions = mutableListOf<Question>()
    questions.add(
      Question(
        1,
        "Which of the following languages is not commonly used to develop Android Apps"
      )
    )
    questions.add(
      Question(
        2,
        "What is the meaning of life?"
      )
    )
    questions.add(
      Question(
        3,
        "Kotlin was developed by which team?"
      )
    )
    return questions
  }

  private fun initAnswersList(): MutableList<Answer> {
    val answers = mutableListOf<Answer>()
    answers.add(
      Answer(
        1,
        1,
        false,
        "Java"
      )
    )
    answers.add(
      Answer(
        2,
        1,
        false,
        "Kotlin"
      )
    )
    answers.add(
      Answer(
        3,
        1,
        true,
        "Ruby"
      )
    )
    answers.add(
      Answer(
        4,
        2,
        true,
        "42"
      )
    )
    answers.add(
      Answer(
        5,
        2,
        false,
        "35"
      )
    )
    answers.add(
      Answer(
        6,
        2,
        false,
        "7"
      )
    )
    answers.add(
      Answer(
        7,
        3,
        false,
        "Google Team"
      )
    )
    answers.add(
      Answer(
        8,
        3,
        true,
        "IntelliJ Team"
      )
    )
    answers.add(
      Answer(
        9,
        3,
        false,
        "Linux Team"
      )
    )
    return answers
  }
}