package at.bachelor.workoutcounter.screens.dataCollectionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.app.ui.components.appbar.AppBar
import at.bachelor.workoutcounter.repository.MetaMotionRepository

@Composable
fun DataCollectionScreen(
    viewModel: DataCollectionViewModel,
    metaMotionRepository: MetaMotionRepository,
    drawerState: DrawerState
) {
    var fileName by remember { mutableStateOf("") }

    val accelerationData = viewModel.accelerationData.collectAsState()
    val gyroData = viewModel.gyroscopeData.collectAsState()

    Scaffold(topBar = { AppBar(drawerState = drawerState) }, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                metaMotionRepository.startSensorAndSave(fileName)
            }

            ) {
                Text(text = stringResource(R.string.start))
            }
            Button(onClick = {
                metaMotionRepository.stopSensor()
            }

            ) {
                Text(text = stringResource(R.string.end))
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            TextField(
                value = fileName,
                onValueChange = { fileName = it },
                label = { Text("Enter file name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )


            Text("Acceleration: " + accelerationData.value)
            Text("Gyroscope: " + gyroData.value)
        }
    }


}

@Preview
@Composable
fun DataCollectionScreenPreview() {
    DataCollectionScreen(
        viewModel = DataCollectionViewModel(),
        metaMotionRepository = MetaMotionRepository(
            context = LocalContext.current,
            dataCollectionViewModel = DataCollectionViewModel()
        ),
        drawerState = DrawerState(DrawerValue.Closed)
    )
}


