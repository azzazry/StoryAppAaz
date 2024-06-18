package com.dicoding.storyappaaz.api.response

import com.dicoding.storyappaaz.model.Story
import com.dicoding.storyappaaz.model.User
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val error: Boolean,
    val message: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: User
)

data class LoginCredentials(
    val email: String,
    val password: String
)

data class RegisterCredentials(
    val name: String,
    val email: String,
    val password: String
)
data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)
data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)

data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
data class StoriesResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("listStory") val data: List<Story>?
)