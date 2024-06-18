package com.dicoding.storyappaaz.api.services

import com.dicoding.storyappaaz.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApi {
    @POST("v1/register")
    fun register(@Body user: RegisterCredentials): Call<RegisterResponse>

    @POST("v1/login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>


    @GET("v1/stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): GetStoriesResponse

    @GET("v1/stories/{id}")
    fun getStoryDetail(@Path("id") storyId: String, @Header("Authorization") token: String): Call<DetailStoryResponse>

    @Multipart
    @POST("v1/stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<FileUploadResponse>

    @GET("v1/stories?location=1")
    fun getLocationStories(@Header("Authorization") token: String): Call<StoriesResponse>
}