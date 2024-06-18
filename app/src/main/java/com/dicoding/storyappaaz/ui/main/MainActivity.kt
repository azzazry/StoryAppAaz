package com.dicoding.storyappaaz.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyappaaz.R
import com.dicoding.storyappaaz.adapter.StoryAdapter
import com.dicoding.storyappaaz.databinding.ActivityMainBinding
import com.dicoding.storyappaaz.ui.authentication.login.LoginActivity
import com.dicoding.storyappaaz.ui.detail.DetailActivity
import com.dicoding.storyappaaz.ui.maps.MapsActivity
import com.dicoding.storyappaaz.ui.post.PostActivity
import com.dicoding.storyappaaz.utils.PreferenceHelper
import com.dicoding.storyappaaz.utils.StoryViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceHelper = PreferenceHelper(this)



        if (!preferenceHelper.isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            viewModel = ViewModelProvider(this, StoryViewModel.ViewModelFactory(this))[StoryViewModel::class.java]
            storyAdapter = StoryAdapter { story ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
                startActivity(intent)
            }

            binding.rvStories.layoutManager = LinearLayoutManager(this)
            binding.rvStories.adapter = storyAdapter

            showLoading(true)
            getData()

        }

        binding.fabPost.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            R.id.action_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                preferenceHelper.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
    private fun getData() {
        showLoading(false)
        viewModel.story.observe(this) { pagingData ->
            lifecycleScope.launch {
                storyAdapter.submitData(pagingData)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}