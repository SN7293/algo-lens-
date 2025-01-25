package com.example.algolens

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SortingVisualizationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val bars = mutableListOf(
        Bar(10), Bar(30), Bar(20), Bar(40), Bar(50), Bar(15), Bar(35), Bar(25)
    )

    private val barPaint = Paint()
    private val textPaint = Paint()

    init {
        textPaint.color = Color.BLACK
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height
        val barWidth = width / bars.size

        for (i in bars.indices) {
            val bar = bars[i]
            val barHeight = (bar.value / 50f) * height // Scale bar height
            val left = i * barWidth.toFloat()
            val top = height - barHeight
            val right = left + barWidth
            val bottom = height.toFloat()

            // Set the color of the bar dynamically
            barPaint.color = bar.color
            canvas?.drawRect(left, top, right, bottom, barPaint)

            // Draw the value inside the bar
            canvas?.drawText(
                bar.value.toString(),
                left + barWidth / 2f,
                bottom - (barHeight / 2f),
                textPaint
            )
        }
    }

    fun updateBars(newBars: List<Bar>) {
        bars.clear()
        bars.addAll(newBars)
        invalidate() // Trigger a redraw
    }
}
