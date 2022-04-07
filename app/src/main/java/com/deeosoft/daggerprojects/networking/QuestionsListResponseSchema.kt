package com.deeosoft.daggerprojects.networking

import com.deeosoft.daggerprojects.model.Question
import com.google.gson.annotations.SerializedName

data class QuestionsListResponseSchema(
    @SerializedName("items") val questions: List<Question>
)
