package at.bachelor.workoutcounter.screens.dataCollectionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbientlab.metawear.data.Acceleration
import com.mbientlab.metawear.data.AngularVelocity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataCollectionViewModel : ViewModel() {
    private val _accelerationData = MutableStateFlow<Acceleration?>(null)
    private val _gyroscopeData = MutableStateFlow<AngularVelocity?>(null)

    val accelerationData: StateFlow<Acceleration?> get() = _accelerationData.asStateFlow()
    val gyroscopeData: StateFlow<AngularVelocity?> get() = _gyroscopeData.asStateFlow()

    fun updateAccelerationData(data: Acceleration) {
        viewModelScope.launch {
            _accelerationData.value = data
        }
    }

    fun updateGyroscopeData(data: AngularVelocity) {
        viewModelScope.launch {
            _gyroscopeData.value = data
        }
    }


}