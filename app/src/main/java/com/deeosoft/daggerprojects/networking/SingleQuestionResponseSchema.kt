package com.deeosoft.daggerprojects.networking

import com.deeosoft.daggerprojects.model.Question
import com.deeosoft.daggerprojects.questions.QuestionWithBody
import com.google.gson.annotations.SerializedName

data class SingleQuestionResponseSchema(
    @SerializedName("items") val questions: List<QuestionWithBody>
){
    val question: QuestionWithBody get() = questions[0]
}