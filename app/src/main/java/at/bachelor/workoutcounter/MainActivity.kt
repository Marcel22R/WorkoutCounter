package at.bachelor.workoutcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.trainingScreen.TrainingViewModel


class MainActivity : ComponentActivity() {
    private val trainingViewModel: TrainingViewModel by viewModels()
    private lateinit var metaMotionRepository: MetaMotionRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metaMotionRepository = MetaMotionRepository(context = this, viewModel = trainingViewModel)
        metaMotionRepository.bindService()
        setContent {
            MainCompose(trainingViewModel, metaMotionRepository)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        metaMotionRepository.unbindService()
    }
}