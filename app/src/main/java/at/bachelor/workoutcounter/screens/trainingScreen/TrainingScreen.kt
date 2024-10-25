package at.bachelor.workoutcounter.screens.trainingScreen

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel
import java.nio.FloatBuffer

@Composable
fun TrainingScreen(
    dataCollection: DataCollectionViewModel = viewModel(),
    metaMotionRepository: MetaMotionRepository
) {
    val context = LocalContext.current
    val accelerationData by dataCollection.accelerationData.collectAsState()
    val gyroscopeData by dataCollection.gyroscopeData.collectAsState()

    val ortEnvironment = remember { OrtEnvironment.getEnvironment() }

    val ortSession = remember {
        createORTSession(ortEnvironment, context)
    }
    val predictionResult= remember {
        mutableStateOf("No prediction yet")
    }

    val combinedData = remember(accelerationData, gyroscopeData) {
        accelerationData?.let {
            gyroscopeData?.let { it1 ->
                floatArrayOf(
                    it.x(), it.y(), it.z(),
                    it1.x(), it1.y(), it1.z()
                )
            }
        }
    }

    LaunchedEffect(combinedData) {
        if (combinedData != null) {
            if (combinedData.isNotEmpty()) {
                val prediction = runPrediction(combinedData, ortSession, ortEnvironment)
                predictionResult.value = "Prediction: $prediction"
                Log.i("onnx", "Prediction: $prediction")
            }
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = predictionResult.value, color = Color.White)

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

fun createORTSession(ortEnvironment: OrtEnvironment, context: android.content.Context): OrtSession {
    val modelBytes =
        context.resources.openRawResource(R.raw.decision_tree_model_6_features).readBytes()
    return ortEnvironment.createSession(modelBytes)
}

fun runPrediction(inputs: FloatArray, ortSession: OrtSession, ortEnvironment: OrtEnvironment): String {
    require(inputs.size == 6) { "The input array must contain exactly 6 float values." }


    val inputName = ortSession.inputNames?.iterator()?.next()


    val floatBufferInputs = FloatBuffer.wrap(inputs)


    val inputTensor = OnnxTensor.createTensor(ortEnvironment, floatBufferInputs, longArrayOf(1, 6))


    val results = ortSession.run(mapOf(inputName to inputTensor))


    val output = results[0].value as Array<String>

    return output[0]
}


