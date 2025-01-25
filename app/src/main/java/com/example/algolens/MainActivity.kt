package com.example.algolens

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var sortingView: SortingVisualizationView
    private lateinit var algorithmSpinner: Spinner
    private lateinit var speedLabel: TextView
    private lateinit var speedControl: SeekBar
    private lateinit var inputArray: EditText
    private lateinit var submitArrayButton: Button

    private var bars: MutableList<Bar> = mutableListOf()
    private var sortingJob: Job? = null
    private var isPaused = false
    private var delayTime: Long = 500 // Default delay time

    companion object {
        val COMPARE_COLOR: Int = Color.parseColor("#FF0000") // Red for comparison
        val NORMAL_COLOR: Int = Color.parseColor("#0000FF") // Blue for normal state
        val SWAP_COLOR: Int = Color.parseColor("#00FF00") // Green for swap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sortingView = findViewById(R.id.sortingView)
        algorithmSpinner = findViewById(R.id.algorithmSpinner)
        speedLabel = findViewById(R.id.speedLabel)
        speedControl = findViewById(R.id.speedControl)
        inputArray = findViewById(R.id.inputArray)
        submitArrayButton = findViewById(R.id.submitArrayButton)

        submitArrayButton.setOnClickListener { handleArrayInput() }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (sortingJob == null || !sortingJob!!.isActive) {
                when (algorithmSpinner.selectedItem.toString()) {
                    "Bubble Sort" -> startBubbleSort()
                    "Selection Sort" -> startSelectionSort()
                    "Insertion Sort" -> startInsertionSort()
                    "Merge Sort" -> startMergeSort()
                    "Quick Sort" -> startQuickSort()
                    "Radix Sort" -> startRadixSort()
                }
                isPaused = false
            }
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            isPaused = true
        }

        findViewById<Button>(R.id.resetButton).setOnClickListener {
            resetBars()
        }

        speedControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                delayTime = (1000 - progress * 10).coerceAtLeast(50).toLong() // Min delay of 50ms
                speedLabel.text = when {
                    progress < 30 -> "Speed: Slow"
                    progress < 70 -> "Speed: Normal"
                    else -> "Speed: Fast"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun handleArrayInput() {
        val arrayInput = inputArray.text.toString().trim()
        if (arrayInput.isEmpty()) {
            Toast.makeText(this, "Please enter a valid array", Toast.LENGTH_SHORT).show()
            return
        }

        val inputValues = arrayInput.split(",").mapNotNull {
            it.trim().toIntOrNull()
        }

        if (inputValues.isEmpty()) {
            Toast.makeText(this, "Please enter valid integer values", Toast.LENGTH_SHORT).show()
            return
        }

        bars = inputValues.map { Bar(it) }.toMutableList()
        sortingView.updateBars(bars)
    }

    private fun startBubbleSort() {
        sortingJob = CoroutineScope(Dispatchers.Main).launch {
            for (i in 0 until bars.size) {
                for (j in 0 until bars.size - i - 1) {
                    while (isPaused) delay(100)
                    bars[j].color = COMPARE_COLOR
                    bars[j + 1].color = COMPARE_COLOR
                    sortingView.updateBars(bars)
                    delay(delayTime)

                    if (bars[j].value > bars[j + 1].value) {
                        bars[j].color = SWAP_COLOR
                        bars[j + 1].color = SWAP_COLOR
                        bars[j] = bars[j + 1].also { bars[j + 1] = bars[j] }
                        sortingView.updateBars(bars)
                        delay(delayTime)
                    }

                    bars[j].color = NORMAL_COLOR
                    bars[j + 1].color = NORMAL_COLOR
                    sortingView.updateBars(bars)
                }
            }
            sortingJob = null
        }
    }

    private fun startSelectionSort() {
        sortingJob = CoroutineScope(Dispatchers.Main).launch {
            for (i in 0 until bars.size - 1) {
                var minIndex = i
                for (j in i + 1 until bars.size) {
                    while (isPaused) delay(100)
                    bars[minIndex].color = COMPARE_COLOR
                    bars[j].color = COMPARE_COLOR
                    sortingView.updateBars(bars)
                    delay(delayTime)

                    if (bars[j].value < bars[minIndex].value) {
                        bars[minIndex].color = NORMAL_COLOR
                        minIndex = j
                        bars[minIndex].color = COMPARE_COLOR
                    } else {
                        bars[j].color = NORMAL_COLOR
                    }
                }

                if (minIndex != i) {
                    bars[minIndex].color = SWAP_COLOR
                    bars[i].color = SWAP_COLOR
                    bars[minIndex] = bars[i].also { bars[i] = bars[minIndex] }
                    sortingView.updateBars(bars)
                    delay(delayTime)
                }

                bars[i].color = NORMAL_COLOR
                bars[minIndex].color = NORMAL_COLOR
                sortingView.updateBars(bars)
            }
            sortingJob = null
        }
    }

    private fun startInsertionSort() {
        sortingJob = CoroutineScope(Dispatchers.Main).launch {
            for (i in 1 until bars.size) {
                var j = i
                while (j > 0 && bars[j - 1].value > bars[j].value) {
                    while (isPaused) delay(100)

                    bars[j].color = SWAP_COLOR
                    bars[j - 1].color = SWAP_COLOR
                    sortingView.updateBars(bars)
                    delay(delayTime)

                    bars[j] = bars[j - 1].also { bars[j - 1] = bars[j] }
                    sortingView.updateBars(bars)

                    bars[j].color = NORMAL_COLOR
                    bars[j - 1].color = NORMAL_COLOR
                    j--
                }
            }
            sortingJob = null
        }
    }

    private fun startMergeSort() {
        sortingJob = CoroutineScope(Dispatchers.Main).launch {
            mergeSort(0, bars.size - 1)
            sortingJob = null
        }
    }

    private suspend fun mergeSort(start: Int, end: Int) {
        if (start < end) {
            val mid = (start + end) / 2
            mergeSort(start, mid)
            mergeSort(mid + 1, end)
            merge(start, mid, end)
        }
    }

    private suspend fun merge(start: Int, mid: Int, end: Int) {
        val temp = mutableListOf<Bar>()
        var i = start
        var j = mid + 1

        while (i <= mid && j <= end) {
            while (isPaused) delay(100)
            bars[i].color = COMPARE_COLOR
            bars[j].color = COMPARE_COLOR
            sortingView.updateBars(bars)
            delay(delayTime)

            if (bars[i].value <= bars[j].value) {
                temp.add(bars[i])
                i++
            } else {
                temp.add(bars[j])
                j++
            }
        }

        while (i <= mid) temp.add(bars[i++])
        while (j <= end) temp.add(bars[j++])

        for (k in temp.indices) {
            bars[start + k] = temp[k]
            bars[start + k].color = SWAP_COLOR
            sortingView.updateBars(bars)
            delay(delayTime)
            bars[start + k].color = NORMAL_COLOR
        }
    }

    private fun startQuickSort() {
        sortingJob = CoroutineScope(Dispatchers.Main).launch {
            quickSort(0, bars.size - 1)
            sortingJob = null
        }
    }

    private suspend fun quickSort(low: Int, high: Int) {
        if (low < high) {
            val pivotIndex = partition(low, high)
            quickSort(low, pivotIndex - 1)
            quickSort(pivotIndex + 1, high)
        }
    }

    private suspend fun partition(low: Int, high: Int): Int {
        val pivot = bars[high]
        pivot.color = COMPARE_COLOR
        var i = low - 1

        for (j in low until high) {
            while (isPaused) delay(100)
            bars[j].color = COMPARE_COLOR
            sortingView.updateBars(bars)
            delay(delayTime)

            if (bars[j].value < pivot.value) {
                i++
                bars[i].color = SWAP_COLOR
                bars[j].color = SWAP_COLOR
                bars[i] = bars[j].also { bars[j] = bars[i] }
                sortingView.updateBars(bars)
                delay(delayTime)
            }

            bars[j].color = NORMAL_COLOR
        }

        bars[i + 1] = bars[high].also { bars[high] = bars[i + 1] }
        sortingView.updateBars(bars)
        delay(delayTime)

        pivot.color = NORMAL_COLOR
        return i + 1
    }

    private fun startRadixSort() {
        sortingJob = CoroutineScope(Dispatchers.Main).launch {
            val max = bars.maxOf { it.value }
            var exp = 1

            while (max / exp > 0) {
                countSort(exp)
                exp *= 10
            }
            sortingJob = null
        }
    }

    private suspend fun countSort(exp: Int) {
        val output = MutableList(bars.size) { Bar(0) }
        val count = IntArray(10) { 0 }

        for (bar in bars) {
            count[(bar.value / exp) % 10]++
        }

        for (i in 1..9) {
            count[i] += count[i - 1]
        }

        for (i in bars.size - 1 downTo 0) {
            output[count[(bars[i].value / exp) % 10] - 1] = bars[i]
            count[(bars[i].value / exp) % 10]--
        }

        for (i in output.indices) {
            bars[i] = output[i]
            bars[i].color = SWAP_COLOR
            sortingView.updateBars(bars)
            delay(delayTime)
            bars[i].color = NORMAL_COLOR
        }
    }

    private fun resetBars() {
        sortingJob?.cancel()
        bars.clear()
        sortingView.updateBars(bars)
        isPaused = false
        sortingJob = null
        inputArray.setText("") 
    }
}
