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
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.createCustomTempFile
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.api.ApiServiceScan
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.response.PredictResponse
import com.dicoding.skinalyzecapstone.databinding.FragmentScanBinding
import com.dicoding.skinalyzecapstone.getImageUri
import com.dicoding.skinalyzecapstone.ui.result.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
                showImage(uri)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private val requestCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                showImage(currentImageUri!!) // Use currentImageUri directly
            } else {
                Log.d("Camera", "Image capture failed")
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("currentImageUri", currentImageUri) // Save the URI
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        // Cek dan minta izin
        if (!allPermissionsGranted()) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
        if (savedInstanceState != null) {
            currentImageUri = savedInstanceState.getParcelable("currentImageUri") // Restore the URI
            currentImageUri?.let { showImage(it) }
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

        binding.clearButton.setOnClickListener {  // Add this line
            clearImage()
        }
        binding.cameraButton.setOnClickListener {
            currentImageUri = getImageUri(requireContext())
            startCamera()
        }
    }

    private fun clearImage() {
        currentImageUri = null
        binding.previewImageView.setImageDrawable(null)
    }

    private fun startGallery() {
        requestGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun startCamera() {
        requestCameraLauncher.launch(currentImageUri)
    }

    private fun showImage(uri: Uri) { // Modified to accept URI as a parameter
        binding.previewImageView.setImageURI(uri)
    }


    private fun postImage(uri: Uri) {

        val file = getFileFromUri(uri) ?: run {
            showToast("Failed to retrieve file from URI.")
            return
        }

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val apiService = ApiConfig.getApiServiceScan()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
                    produceFile = { requireContext().dataStoreFile("user_prefs.preferences_pb") }
                )

                // Ambil ID user dari UserPreference dan tampilkan di log
                val userId = UserPreference.getInstance(dataStore).getSession().first().idUser
                Log.d("ScanFragment", "User ID melakukan scan: $userId")

                val idUserRequestBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = apiService.predictImage(body, idUserRequestBody)

                // Log the response for debugging
                Log.d("ScanFragment", "User ID received by API: $userId")

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
            putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri) // Add this line
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
