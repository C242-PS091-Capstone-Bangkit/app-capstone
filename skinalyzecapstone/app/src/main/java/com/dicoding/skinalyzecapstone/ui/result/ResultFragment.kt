package com.dicoding.skinalyzecapstone.ui.result;


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.skinalyzecapstone.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var imageUri: Uri? = null

    companion object {
        const val ARG_IMAGE_URI = "image_uri"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)

        // Retrieve the image URI from arguments
        arguments?.getString(ARG_IMAGE_URI)?.let {
            imageUri = Uri.parse(it)
        }

        // Display the image
        imageUri?.let {
            binding.resultImageView.setImageURI(it)
            showToast("Image successfully loaded for analysis!")
        } ?: showToast("No image found")

        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
