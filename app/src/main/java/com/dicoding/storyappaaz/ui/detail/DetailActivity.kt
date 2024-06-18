package com.dicoding.storyappaaz.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.storyappaaz.R
import com.dicoding.storyappaaz.api.response.DetailStoryResponse
import com.dicoding.storyappaaz.api.services.ApiConfig
import com.dicoding.storyappaaz.api.services.StoryApi
import com.dicoding.storyappaaz.databinding.ActivityDetailBinding
import com.dicoding.storyappaaz.model.Story
import com.dicoding.storyappaaz.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var storyApi: StoryApi
    private var storyId: String? = null
    private var apiConfig: ApiConfig = ApiConfig()
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyId = intent.getStringExtra(EXTRA_STORY_ID)
        preferenceHelper = PreferenceHelper(this)

        storyApi = apiConfig.getApiService()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        loadStoryDetail()
    }

    private fun loadStoryDetail() {
        val token = "Bearer ${preferenceHelper.token}"
        storyId?.let { id ->
            storyApi.getStoryDetail(id, token).enqueue(object : Callback<DetailStoryResponse> {
                override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                    if (response.isSuccessful) {
                        val story = response.body()?.story
                        update(story)
                    } else {
                        Toast.makeText(this@DetailActivity, "Failed to load story detail", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    private fun update(story: Story?) {
        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description

            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.ivDetailPhoto)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return true
    }
    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}