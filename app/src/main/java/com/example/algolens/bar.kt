package com.example.algolens

import android.graphics.Color

data class Bar(
    val value: Int,
    var color: Int = NORMAL_COLOR // Default color
)

// Define color constants
val NORMAL_COLOR = Color.argb(150, 135, 206, 250)  // Light blue
val COMPARE_COLOR = Color.YELLOW                   // Yellow for comparison
val SWAP_COLOR = Color.RED                         // Red for swapping
