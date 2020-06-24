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

package com.raywenderlich.droidquiz.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raywenderlich.droidquiz.data.migrations.Migration1To2
import com.raywenderlich.droidquiz.data.migrations.Migration1To4
import com.raywenderlich.droidquiz.data.migrations.Migration2To3
import com.raywenderlich.droidquiz.data.migrations.Migration3To4
import com.raywenderlich.droidquiz.data.model.Answer
import com.raywenderlich.droidquiz.data.model.Question

@Database(entities = [(Question::class), (Answer::class)], version = 4)
abstract class QuizDatabase : RoomDatabase() {
  companion object {
    val MIGRATION_1_TO_2 = Migration1To2()
    val MIGRATION_2_TO_3 = Migration2To3()
    val MIGRATION_3_TO_4 = Migration3To4()
    val MIGRATION_1_TO_4 = Migration1To4()
  }

  abstract fun questionsDao(): QuestionDao
}