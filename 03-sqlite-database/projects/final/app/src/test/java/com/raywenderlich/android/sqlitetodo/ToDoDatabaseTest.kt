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
 */

package com.raywenderlich.android.sqlitetodo

import com.raywenderlich.android.sqlitetodo.controller.ToDoDatabaseHandler
import com.raywenderlich.android.sqlitetodo.model.ToDo
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TestDatabase {
  // 1
  lateinit var dbHelper: ToDoDatabaseHandler

  // 2
  @Before
  fun setup() {
    dbHelper = ToDoDatabaseHandler(RuntimeEnvironment.application)
    dbHelper.recreateEmptyDatabase()
  }


  @Test
  @Throws(Exception::class)
  fun testDatabaseInsertion() {

    // 3
    // Given
    val item1 = ToDo(0, "Test my Program", false)
    val item2 = ToDo(0, "Test my Program Again", false)

    // 4
    // When
    dbHelper.createToDo(item1)
    dbHelper.createToDo(item2)

    // 5
    // Then
    val allText = dbHelper.getAllText()
    val expectedValue = "${item1.toDoName}${item2.toDoName}"
    Assert.assertEquals(allText, expectedValue)
  }

  // 6
  @After
  fun reset() {
    dbHelper.clearDatabase()
  }
}


