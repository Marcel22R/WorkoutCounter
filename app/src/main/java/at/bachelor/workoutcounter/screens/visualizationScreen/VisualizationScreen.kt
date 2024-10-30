package at.bachelor.workoutcounter.screens.visualizationScreen

import LowPassRunningAverageFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.delay
import kotlin.math.sqrt

@Composable
fun VisualizationScreen(
    dataCollection: DataCollectionViewModel = viewModel(),
    metaMotionRepository: MetaMotionRepository
) {
    val accelerationData by dataCollection.accelerationData.collectAsState()
    val gyroscopeData by dataCollection.gyroscopeData.collectAsState()

    // Define window size for the moving average filter
    val windowSize = 100
    val alpha = 0.1f
    val errorWhenNoMovement = 0.14f

    // Remember list to accumulate entries for the chart
    val filteredAccRData = remember { mutableStateListOf<Entry>() }
    val filter = remember { LowPassRunningAverageFilter(windowSize, alpha) }
    val indexAfterRemove = remember { mutableIntStateOf(0) }
    val accR = remember { mutableFloatStateOf(0.0f) }

    LaunchedEffect(accelerationData) {
        accelerationData?.let { acc ->
            accR.floatValue = calculateAccR(acc.x(), acc.y(), acc.z())
            if (accR.floatValue < errorWhenNoMovement) {
                accR.floatValue = 0.0f
            }

            val filteredValue = filter.filter(accR.floatValue)

            filteredAccRData.add(
                Entry(
                    filteredAccRData.size.toFloat() + indexAfterRemove.intValue,
                    filteredValue
                )
            )
            if (filteredAccRData.size > 1000) {
                indexAfterRemove.intValue++
                filteredAccRData.removeAt(0)
            }

            delay(100) // Simulate real-time data feed
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = accelerationData.toString(), color = Color.White)
        Text(text = gyroscopeData.toString(), color = Color.White)
        Text(text = "Acc_r: " + accR.floatValue.toString(), color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { context ->
                LineChart(context).apply {
                    description = Description().apply { text = "Filtered acc_r over Time" }
                    setDrawGridBackground(false)
                    axisRight.isEnabled = false
                }
            },
            update = { chart ->
                val dataSet = LineDataSet(filteredAccRData, "Filtered acc_r").apply {
                    lineWidth = 2f
                    color = Color.Blue.toArgb()
                    setDrawCircles(false)
                    setDrawValues(false)
                }

                chart.data = LineData(dataSet)
                chart.notifyDataSetChanged()
                chart.invalidate() // Refresh the chart
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { metaMotionRepository.startSensor() }) {
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { metaMotionRepository.stopSensor() }) {
            Text(text = "Stop")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun calculateAccR(accX: Float, accY: Float, accZ: Float): Float {
    return sqrt(accX * accX + accY * accY + accZ * accZ)
}