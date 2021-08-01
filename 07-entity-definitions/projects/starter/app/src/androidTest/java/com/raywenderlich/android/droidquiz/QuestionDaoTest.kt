package com.raywenderlich.android.droidquiz

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.droidquiz.data.db.QuestionDao
import com.raywenderlich.android.droidquiz.data.db.QuizDatabase
import com.raywenderlich.android.droidquiz.data.model.Question
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionDaoTest {
  @Rule
  @JvmField
  val rule: TestRule = InstantTaskExecutorRule()

  private lateinit var database: QuizDatabase
  private lateinit var questionDao: QuestionDao

  @Before
  fun setUp() {
    val context: Context = InstrumentationRegistry.getInstrumentation().context
    try {
      database = Room.inMemoryDatabaseBuilder(context, QuizDatabase::class.java)
          .allowMainThreadQueries()
          .build()
    }catch (e: Exception){
      Log.i("test", e.message)
    }
    questionDao = database.questionsDao()
  }

  @Test
  fun testDatabase(){
    val preNumberOfQuestions = questionDao.getAllQuestions()
    val question = Question(1,"Como te llamas")
    questionDao.insert(question)
    val postNumberOfQuestions = questionDao.getAllQuestions()
    Assert.assertEquals(1, postNumberOfQuestions.size - preNumberOfQuestions.size)
    questionDao.clearQuestions(question)
    Assert.assertEquals(0, questionDao.getAllQuestions().size)
  }

  @After
  fun tearDown(){
    database.close()
  }
}