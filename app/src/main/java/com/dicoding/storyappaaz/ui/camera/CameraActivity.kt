package com.dicoding.storyappaaz.ui.camera

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import com.dicoding.storyappaaz.databinding.ActivityCameraBinding
import com.dicoding.storyappaaz.utils.`object`.CameraUtils
import com.dicoding.storyappaaz.utils.`object`.Constants

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCamera(CameraSelector.DEFAULT_BACK_CAMERA)

        binding.captureImage.setOnClickListener {
            imageCapture?.let {
                CameraUtils.takePhoto(this, it, application, cameraSelector) { success, file ->
                    if (success && file != null) {
                        val intent = Intent().apply {
                            putExtra("picture", file)
                            putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        }
                        setResult(Constants.CAMERA_X_RESULT, intent)
                        finish()
                    }
                }
            }
        }

        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA
            setupCamera(cameraSelector)
        }
    }

    private fun setupCamera(cameraSelector: CameraSelector) {
        CameraUtils.startCamera(application, this, binding.viewFinder.surfaceProvider, cameraSelector) { capture ->
            imageCapture = capture
        }
    }
}