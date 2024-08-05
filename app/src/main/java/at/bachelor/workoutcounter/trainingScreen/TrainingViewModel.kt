package at.bachelor.workoutcounter.trainingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbientlab.metawear.data.Quaternion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val _quaternionData = MutableStateFlow<Quaternion?>(null)
    val data: StateFlow<Quaternion?> get() = _quaternionData.asStateFlow()

    fun updateAccelerationData(data: Quaternion) {
        viewModelScope.launch {
            _quaternionData.value = data
        }
    }

}