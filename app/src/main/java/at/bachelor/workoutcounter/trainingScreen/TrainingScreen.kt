package at.bachelor.workoutcounter.trainingScreen

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

@Composable
fun TrainingScreen(trainingViewModel: TrainingViewModel = viewModel(),startAccelerometer: () -> Unit, stopAccelerometer: () -> Unit) {
    val data by trainingViewModel.data.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = data.toString(), color = Color.White)

        Spacer(modifier = Modifier.height(16.dp)) // Adding space between the text and the buttons

        Button(onClick = startAccelerometer) {
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(8.dp)) // Adding space between the buttons

        Button(onClick = stopAccelerometer) {
            Text(text = "Stop")
        }
    }
}