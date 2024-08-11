package at.bachelor.workoutcounter.repository

import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel
import com.mbientlab.metawear.MetaWearBoard
import com.mbientlab.metawear.android.BtleService
import com.mbientlab.metawear.android.BtleService.LocalBinder
import com.mbientlab.metawear.data.Acceleration
import com.mbientlab.metawear.data.AngularVelocity
import com.mbientlab.metawear.module.Accelerometer
import com.mbientlab.metawear.module.GyroBmi160
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MetaMotionRepository(
    private val context: Context,
    private val dataCollectionViewModel: DataCollectionViewModel,
) {
    private lateinit var board: MetaWearBoard
    private lateinit var accelerationData: Accelerometer
    private lateinit var gyroscopeData: GyroBmi160
    private lateinit var serviceBinder: LocalBinder
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = service as LocalBinder
            retrieveBoard(context.getString(R.string.mac_address))
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceBound = false
        }


    }

    private fun retrieveBoard(macAddress: String) {
        Log.i("imu", "Trying to retrieve board")
        val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val remoteDevice = btManager.adapter.getRemoteDevice(macAddress)
        board = serviceBinder.getMetaWearBoard(remoteDevice)

        board.connectAsync().onSuccessTask<Void> {
            Log.i("imu", "Connected to $macAddress")
            accelerationData = board.getModule(Accelerometer::class.java)
            gyroscopeData = board.getModule(GyroBmi160::class.java)
            null
        }.continueWith { task ->
            if (task.isFaulted) {
                Log.e("imu", "Board failed to connect", task.error)
            } else {
                Log.i("imu", "Board connection successful, ready to start sensor")
            }
        }
    }


    private fun saveAccelerationData(acceleration: Acceleration) {
        val file = File(context.getExternalFilesDir(null), "acceleration_data.csv")
        try {
            val writer = BufferedWriter(FileWriter(file, true))
            writer.write("${System.currentTimeMillis()},${acceleration.x()},${acceleration.y()},${acceleration.z()}\n")
            writer.close()
        } catch (e: IOException) {
            Log.e("imu", "Error writing acceleration data to CSV", e)
        }
    }

    private fun saveAngularVelocityData(angularVelocity: AngularVelocity) {
        val file = File(context.getExternalFilesDir(null), "angular_velocity_data.csv")
        try {
            val writer = BufferedWriter(FileWriter(file, true))
            writer.write("${System.currentTimeMillis()},${angularVelocity.x()},${angularVelocity.y()},${angularVelocity.z()}\n")
            writer.close()
        } catch (e: IOException) {
            Log.e("imu", "Error writing angular velocity data to CSV", e)
        }
    }


    fun bindService() {
        val intent = Intent(context, BtleService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        isServiceBound = true
    }

    fun unbindService() {
        if (isServiceBound) {
            context.unbindService(serviceConnection)
            isServiceBound = false
        }
    }


    private fun setupSavingRoutes() {
        Log.i("imu", "Setting up saving routes")
        accelerationData.acceleration().addRouteAsync { source ->
            source.stream { data, _ ->
                val acceleration = data.value(Acceleration::class.java)
                Log.i("imu", "Acceleration: $acceleration")
                dataCollectionViewModel.updateAccelerationData(acceleration)
                saveAccelerationData(acceleration)
            }
        }



        gyroscopeData.angularVelocity().addRouteAsync { source ->
            source.stream { data, _ ->
                val angularVelocity = data.value(AngularVelocity::class.java)
                Log.i("imu", "Angular velocity: $angularVelocity")
                dataCollectionViewModel.updateGyroscopeData(angularVelocity)
                saveAngularVelocityData(angularVelocity)
            }
        }

    }

    private fun setupStreamingRoutes() {
        Log.i("imu", "Setting up streaming routes")
        accelerationData.acceleration().addRouteAsync { source ->
            source.stream { data, _ ->
                Log.i("imu", "Acceleration: ${data.value(Acceleration::class.java)}")
                dataCollectionViewModel.updateAccelerationData(data.value(Acceleration::class.java))
            }
        }
        gyroscopeData.angularVelocity().addRouteAsync { source ->
            source.stream { data, _ ->
                Log.i("imu", "Angular velocity: ${data.value(AngularVelocity::class.java)}")
                dataCollectionViewModel.updateGyroscopeData(data.value(AngularVelocity::class.java))
            }

        }
    }


    fun startSensor(saveData: Boolean) {
        if (::board.isInitialized) {
            if (saveData) {
                setupSavingRoutes()
            } else {
                setupStreamingRoutes()
            }
            accelerationData.start()
            gyroscopeData.start()
            accelerationData.acceleration().start()
            gyroscopeData.angularVelocity().start()
            Log.i("imu", "Data collection started with saveData=$saveData")
        } else {
            Log.e("imu", "Sensor not initialized")
        }
    }

    fun stopSensor() {
        if (::accelerationData.isInitialized && ::gyroscopeData.isInitialized) {
            accelerationData.acceleration().stop()
            gyroscopeData.angularVelocity().stop()
            accelerationData.stop()
            gyroscopeData.stop()
            Log.i("imu", "Data collection stopped")
        } else {
            Log.e("imu", "Sensor not initialized")
        }
    }
}