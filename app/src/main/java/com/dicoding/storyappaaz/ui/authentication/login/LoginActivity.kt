package com.dicoding.storyappaaz.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.storyappaaz.R
import com.dicoding.storyappaaz.animation.applyFadeInAnimations
import com.dicoding.storyappaaz.api.response.LoginCredentials
import com.dicoding.storyappaaz.api.response.LoginResponse
import com.dicoding.storyappaaz.api.services.ApiConfig
import com.dicoding.storyappaaz.databinding.ActivityLoginBinding
import com.dicoding.storyappaaz.ui.authentication.register.RegisterActivity
import com.dicoding.storyappaaz.ui.main.MainActivity
import com.dicoding.storyappaaz.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferenceHelper: PreferenceHelper
    private val apiConfig: ApiConfig = ApiConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferenceHelper = PreferenceHelper(this)
        val button = listOf(
            binding.edLoginEmail,
            binding.edLoginPassword,
            binding.btnLogin,
            binding.btnOpenRegister
        )
        applyFadeInAnimations(this, button)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.editText?.text.toString()
            val password = binding.edLoginPassword.editText?.text.toString()

            val loginCredentials = LoginCredentials(email, password)
            val call = apiConfig.getApiService().login(loginCredentials)

            showLoading(true)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.error == false) {
                            preferenceHelper.isLoggedIn = true
                            preferenceHelper.token = loginResponse.loginResult.token
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                loginResponse?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.btnOpenRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        val loadingOverlay = findViewById<FrameLayout>(R.id.loading_overlay)
        if (isLoading) {
            loadingOverlay.visibility = View.VISIBLE
        } else {
            loadingOverlay.visibility = View.GONE
        }
    }
}