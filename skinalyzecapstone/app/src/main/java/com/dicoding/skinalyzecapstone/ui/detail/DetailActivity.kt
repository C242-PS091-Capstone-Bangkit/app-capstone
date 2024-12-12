package com.dicoding.skinalyzecapstone.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.skinalyzecapstone.data.response.PredictResponse
import com.dicoding.skinalyzecapstone.data.response.RecommendationItem
import com.dicoding.skinalyzecapstone.databinding.ActivityDetailBinding
import com.dicoding.skinalyzecapstone.ui.setreminder.SetReminderActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_RECOMMENDATION = "extra_recommendation"
        const val EXTRA_SKIN_CONDITION = "extra_skin_condition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recommendation = intent.getParcelableExtra<RecommendationItem>(EXTRA_RECOMMENDATION)
        // In DetailActivity.kt

        val skinCondition = intent.getStringExtra(EXTRA_SKIN_CONDITION) // Get the string extra

        recommendation?.let {

            Glide.with(this).load(it.gambarProduk).into(binding.productImage)
            binding.productName.text = it.namaProduk
            binding.productDescription.text = it.saranKandungan // Ganti dengan deskripsi produk jika ada
            val descriptionText = "${it.namaProduk} is perfect for those experiencing ${skinCondition} skin conditions. " +
                    "You can find more information about this product [here] if you want to buy or know about the product." +
                    "\n\nSet Reminder for your desired product by clicking the button below!"

            val spannableString = SpannableString(descriptionText)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.linkProduk))
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false // Menghilangkan garis bawah pada link
                    ds.color = ds.linkColor // Mengatur warna link
                }
            }

            val startIndex = descriptionText.indexOf("[here]")
            val endIndex = startIndex + "[here]".length
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.productDescription.text = spannableString
            binding.productDescription.movementMethod = LinkMovementMethod.getInstance()

            binding.reminderButton.setOnClickListener {
                val intent = Intent(this, SetReminderActivity::class.java)
                startActivity(intent)
            }

        }
    }
}