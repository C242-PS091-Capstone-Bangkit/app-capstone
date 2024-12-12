package com.dicoding.skinalyzecapstone.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.createCustomTempFile
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.api.ApiServiceScan
import com.dicoding.skinalyzecapstone.data.response.PredictResponse
import com.dicoding.skinalyzecapstone.databinding.FragmentScanBinding
import com.dicoding.skinalyzecapstone.getImageUri
import com.dicoding.skinalyzecapstone.ui.result.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding
    private var currentImageUri: Uri? = null

    private val requestGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private val requestCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                showImage()
            } else {
                Log.d("Camera", "Image capture failed")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        // Cek dan minta izin
        if (!allPermissionsGranted()) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                postImage(it)
            } ?: showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun startGallery() {
        requestGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        requestCameraLauncher.launch(currentImageUri)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun postImage(uri: Uri) {
        val file = getFileFromUri(uri) ?: run {
            showToast("Failed to retrieve file from URI.")
            return
        }

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val idUser = "1".toRequestBody("text/plain".toMediaTypeOrNull())

        val apiService = ApiConfig.getApiServiceScan()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.predictImage(body, idUser)

                // Log the response for debugging
                Log.d("ScanFragment", "API Response: $response")

                launch(Dispatchers.Main) {
                    navigateToResultActivity(response)
                }
            } catch (e: Exception) {
                Log.e("ScanFragment", "Error: ${e.message}", e)
                launch(Dispatchers.Main) {
                    showToast("Failed to analyze image. Try again.")
                }
            }
        }
    }

    private fun navigateToResultActivity(response: PredictResponse) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_RESULT, response)
        }
        startActivity(intent)
    }

    private fun getFileFromUri(uri: Uri): File? {
        val tempFile = createCustomTempFile(requireContext())
        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1001
    }
}
