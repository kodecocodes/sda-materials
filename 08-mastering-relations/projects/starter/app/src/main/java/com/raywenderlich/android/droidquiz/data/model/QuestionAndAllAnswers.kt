package com.raywenderlich.android.droidquiz.data.model

import androidx.room.Embedded
import androidx.room.Relation


class QuestionAndAllAnswers {
    @Embedded//1
    var question: Question? = null

    @Relation(
        parentColumn = "question_id",//2
        entityColumn = "question_id"
    )
    var answers: List<Answer> = ArrayList()//3
}
