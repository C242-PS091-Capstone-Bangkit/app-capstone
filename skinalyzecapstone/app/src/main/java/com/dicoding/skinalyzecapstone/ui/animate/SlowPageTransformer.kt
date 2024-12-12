package com.dicoding.skinalyzecapstone.ui.animate

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class SlowPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val absPosition = Math.abs(position)
            scaleX = 1 - (0.2f * absPosition)
            scaleY = 1 - (0.2f * absPosition)
            alpha = 0.5f + (1 - absPosition)
        }
    }
}
