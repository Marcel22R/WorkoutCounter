package at.bachelor.workoutcounter.repository

import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.screens.trainingScreen.TrainingViewModel
import com.mbientlab.metawear.MetaWearBoard
import com.mbientlab.metawear.android.BtleService
import com.mbientlab.metawear.android.BtleService.LocalBinder
import com.mbientlab.metawear.data.Quaternion
import com.mbientlab.metawear.module.SensorFusionBosch
import com.mbientlab.metawear.module.SensorFusionBosch.Mode

class MetaMotionRepository(
    private val context: Context,
    private val viewModel: TrainingViewModel
) {
    private lateinit var board: MetaWearBoard
    private lateinit var sensorFusionBosch: SensorFusionBosch
    private lateinit var serviceBinder: LocalBinder
    private var isServiceBound = false


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = service as LocalBinder
            Log.i("imu", "Service connected")
            retrieveBoard(context.getString(R.string.mac_address))
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceBound = false
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

    private fun retrieveBoard(macAddress: String) {
        val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val remoteDevice = btManager.adapter.getRemoteDevice(macAddress)
        board = serviceBinder.getMetaWearBoard(remoteDevice)
        board.connectAsync().onSuccessTask { task ->
            Log.i("imu", "Connected to $macAddress")
            sensorFusionBosch = board.getModule(SensorFusionBosch::class.java)
            sensorFusionBosch.configure()
                .mode(Mode.NDOF)
                .accRange(SensorFusionBosch.AccRange.AR_16G)
                .gyroRange(SensorFusionBosch.GyroRange.GR_2000DPS)
                .commit()
            sensorFusionBosch.quaternion().addRouteAsync { source ->
                source.stream { data, _ ->
                    Log.i(
                        "imu",
                        "Acceleration: ${data.value(Quaternion::class.java)}"
                    )
                    viewModel.updateAccelerationData(data.value(Quaternion::class.java))
                }
            }
        }.continueWith { task ->
            if (task.isFaulted) {
                Log.i("imu", "Board failed to connect")
            } else {
                Log.i("imu", "Route setup completed")
            }
            null
        }

    }

    fun startAccelerometer() {
        if (::sensorFusionBosch.isInitialized) {
            sensorFusionBosch.start()
            sensorFusionBosch.quaternion().start()
            Log.i("imu", "Quaternions started")
        } else {
            Log.e("imu", "Sensorfusion not initialized")
        }
    }

    fun stopAccelerometer() {
        if (::sensorFusionBosch.isInitialized) {
            sensorFusionBosch.quaternion().stop()
            sensorFusionBosch.stop()
            Log.i("imu", "Quaternions stopped")
        } else {
            Log.e("imu", "Bosch not initialized")
        }
    }


}