package at.bachelor.workoutcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel


class MainActivity : ComponentActivity() {
    private val dataCollectionViewModel: DataCollectionViewModel by viewModels()
    private lateinit var metaMotionRepository: MetaMotionRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metaMotionRepository = MetaMotionRepository(
            context = this,
            dataCollectionViewModel = dataCollectionViewModel
        )
        metaMotionRepository.bindService()
        setContent {
            MainCompose(dataCollectionViewModel, metaMotionRepository)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        metaMotionRepository.unbindService()
    }
}