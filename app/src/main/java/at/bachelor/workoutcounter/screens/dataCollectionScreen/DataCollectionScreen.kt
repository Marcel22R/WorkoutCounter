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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.app.ui.components.appbar.AppBar
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
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
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        LocalDateTime.now().format(formatter)
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var expandedExercise by remember { mutableStateOf(false) }
    var expandedIntensity by remember { mutableStateOf(false) }

    val exerciseOptions = listOf(
        "Flat-bench-press",
        "Squats",
        "Romanian-deadlift",
        "Arnold-press",
        "Lat-Pulldown",
        "Break"
    )
    val intensityOptions = listOf("Heavy", "Light", "Standing", "Sitting")

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
                        fileNameAdvanced =
                            exerciseName + "_" + personName + "_" + intensity + "_" + setCount + "_" + currentDateTime
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
            when (selectedTabIndex) {
                0 -> {
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
                    ExposedDropdownMenuBox(
                        expanded = expandedExercise,
                        onExpandedChange = { expandedExercise = it }) {
                        TextField(
                            value = exerciseName,
                            label = { Text(text = "Choose exercise") },
                            readOnly = true,
                            onValueChange = {},
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedExercise) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { expandedExercise = true }
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedExercise,
                            onDismissRequest = { expandedExercise = false }) {
                            exerciseOptions.forEach { exercise ->
                                DropdownMenuItem(
                                    text = { Text(exercise) },
                                    onClick = {
                                        exerciseName = exercise
                                        expandedExercise = false
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.White
                                    )
                                )
                            }
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
                    ExposedDropdownMenuBox(
                        expanded = expandedIntensity,
                        onExpandedChange = { expandedIntensity = it }) {
                        TextField(
                            value = intensity,
                            label = { Text(text = "Choose intensity") },
                            readOnly = true,
                            onValueChange = {},
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedIntensity) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { expandedIntensity = true }
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedIntensity,
                            onDismissRequest = { expandedIntensity = false }) {
                            intensityOptions.forEach { intensityOption ->
                                DropdownMenuItem(
                                    text = { Text(intensityOption) },
                                    onClick = {
                                        intensity = intensityOption
                                        expandedIntensity = false
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.White
                                    )
                                )
                            }
                        }
                    }


                    // Number entry for set count
                    TextField(
                        value = setCount,
                        onValueChange = {
                            setCount = it.filter { it.isDigit() }
                        },  // Ensure only digits are entered
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
