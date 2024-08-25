package at.bachelor.workoutcounter.screens.trainingScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel

@Composable
fun TrainingScreen(
    dataCollection: DataCollectionViewModel = viewModel(),
    metaMotionRepository: MetaMotionRepository
) {
    val accelerationData by dataCollection.accelerationData.collectAsState()
    val gyroscopeData by dataCollection.gyroscopeData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = accelerationData.toString(), color = Color.White)
        Text(text = gyroscopeData.toString(), color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { metaMotionRepository.startSensor() }) {
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { metaMotionRepository.stopSensor() }) {
            Text(text = "Stop")
        }
    }
}