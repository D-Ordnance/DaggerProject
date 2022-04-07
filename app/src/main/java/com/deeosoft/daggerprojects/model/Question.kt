package com.deeosoft.daggerprojects.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Question(
    @SerializedName("title") var title: String,
    @SerializedName("question_id") var id: String,
): Serializable
