class LowPassRunningAverageFilter(private val windowSize: Int, private val alpha: Float = 0.1f) {
    private val dataWindow = ArrayDeque<Float>()
    private var sum = 0f
    private var previousFilteredValue: Float? = null

    fun filter(newDataPoint: Float): Float {
        dataWindow.add(newDataPoint)
        sum += newDataPoint

        if (dataWindow.size > windowSize) {
            sum -= dataWindow.removeFirst()
        }

        val runningAverage = sum / dataWindow.size

        val filteredValue = previousFilteredValue?.let { it * (1 - alpha) + runningAverage * alpha } ?: runningAverage
        previousFilteredValue = filteredValue

        return filteredValue
    }
}
