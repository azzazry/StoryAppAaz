package com.dicoding.storyappaaz.utils.objects

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.storyappaaz.utils.createFile
import java.io.File

object CameraUtils {

    fun startCamera(application: Application, context: Context, viewFinder: Preview.SurfaceProvider, cameraSelector: CameraSelector, callback: (ImageCapture?) -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(viewFinder)
            }

            val imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    context as AppCompatActivity,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                callback(imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(context, "Gagal memunculkan kamera.", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(context: Context, imageCapture: ImageCapture, application: Application, cameraSelector: CameraSelector, resultCallback: (Boolean, File?) -> Unit) {
        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    resultCallback(false, null)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    resultCallback(true, photoFile)
                }
            }
        )
    }
}