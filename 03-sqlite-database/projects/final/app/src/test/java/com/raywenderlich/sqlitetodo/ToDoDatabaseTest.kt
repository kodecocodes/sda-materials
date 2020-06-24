package com.raywenderlich.sqlitetodo

import com.raywenderlich.sqlitetodo.Controller.ToDoDatabaseHandler
import com.raywenderlich.sqlitetodo.Model.ToDo
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


