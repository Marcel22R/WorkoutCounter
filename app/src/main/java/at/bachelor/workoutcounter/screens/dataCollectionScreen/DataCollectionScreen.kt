package at.bachelor.workoutcounter.screens.dataCollectionScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DataCollectionScreen(
    viewModel: DataCollectionViewModel,
    metaMotionRepository: MetaMotionRepository,
    drawerState: DrawerState
) {
    var fileNameSimple by remember { mutableStateOf("") }
    var fileNameAdvanced by remember { mutableStateOf("") }
    var exerciseName by remember { mutableStateOf("") }
    var personName by remember { mutableStateOf("") }
    var intensity by remember { mutableStateOf("") }
    var setCount by remember { mutableStateOf("") }

    // Getting the current date and time formatted
    val currentDateTime = remember {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        LocalDateTime.now().format(formatter)
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var expandedExercise by remember { mutableStateOf(false) }
    var expandedIntensity by remember { mutableStateOf(false) }

    val exerciseOptions = listOf("Push-up", "Squat", "Deadlift", "Bench Press", "Pull-up")
    val intensityOptions = listOf("High", "Low")

    val accelerationData = viewModel.accelerationData.collectAsState()
    val gyroData = viewModel.gyroscopeData.collectAsState()

    Scaffold(
        topBar = { AppBar(drawerState = drawerState) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (selectedTabIndex == 0) {
                        metaMotionRepository.startSensorAndSave(fileNameSimple)
                    } else {
                        metaMotionRepository.startSensorAndSave(fileNameAdvanced)
                    }
                }) {
                    Text(text = stringResource(R.string.start))
                }
                Button(onClick = {
                    metaMotionRepository.stopSensor()
                }) {
                    Text(text = stringResource(R.string.end))
                }
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // TabRow for switching between Simple and Advanced modes
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Simple") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Advanced") }
                )
            }

            // Content based on the selected tab
            when (selectedTabIndex) {
                0 -> {
                    // Simple text entry mode
                    TextField(
                        value = fileNameSimple,
                        onValueChange = { fileNameSimple = it },
                        label = { Text("Enter file name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                1 -> {
                    // Advanced mode with dropdowns and additional fields
                    // Dropdown for exercise name
                    TextField(
                        value = exerciseName,
                        onValueChange = {},
                        label = { Text("Exercise Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { expandedExercise = true }
                    )
                    DropdownMenu(
                        expanded = expandedExercise,
                        onDismissRequest = { expandedExercise = false }
                    ) {
                        exerciseOptions.forEach { exercise ->
                            DropdownMenuItem(
                                text = { Text(exercise) },
                                onClick = {
                                    exerciseName = exercise
                                    expandedExercise = false
                                }
                            )
                        }
                    }

                    // TextField for person's name
                    TextField(
                        value = personName,
                        onValueChange = { personName = it },
                        label = { Text("Person Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    // Dropdown for intensity
                    TextField(
                        value = intensity,
                        onValueChange = {},
                        label = { Text("Intensity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { expandedIntensity = true }
                    )
                    DropdownMenu(
                        expanded = expandedIntensity,
                        onDismissRequest = { expandedIntensity = false }
                    ) {
                        intensityOptions.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    intensity = level
                                    expandedIntensity = false
                                }
                            )
                        }
                    }

                    // Number entry for set count
                    TextField(
                        value = setCount,
                        onValueChange = { setCount = it.filter { it.isDigit() } },  // Ensure only digits are entered
                        label = { Text("Set Count") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    // Display current date and time
                    TextField(
                        value = currentDateTime,
                        onValueChange = {},
                        label = { Text("Date and Time") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            // Display acceleration and gyroscope data regardless of the tab
            Text("Acceleration: ${accelerationData.value}")
            Text("Gyroscope: ${gyroData.value}")
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
