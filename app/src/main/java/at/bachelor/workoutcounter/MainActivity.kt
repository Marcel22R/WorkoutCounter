package at.bachelor.workoutcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.trainingScreen.TrainingViewModel


class MainActivity : ComponentActivity() {

//    private lateinit var accelerometer: Accelerometer
//    private lateinit var serviceBinder: BtleService.LocalBinder
//    private lateinit var board: MetaWearBoard
//    private val trainingViewModel: TrainingViewModel by viewModels()

    private val trainingViewModel: TrainingViewModel by viewModels()
    private lateinit var metaMotionRepository: MetaMotionRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metaMotionRepository = MetaMotionRepository(context = this, viewModel = trainingViewModel)
        metaMotionRepository.bindService()
        setContent {
            MainCompose(trainingViewModel, metaMotionRepository)
        }

//        TrainingScreen(trainingViewModel, { startAccelerometer() }, { stopAccelerometer() }) this works
//        val intent = Intent(this, BtleService::class.java)
//        applicationContext.bindService(intent, serviceConnection, BIND_AUTO_CREATE)

    }


    override fun onDestroy() {
        super.onDestroy()
        metaMotionRepository.unbindService()
    }

//    private val serviceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            serviceBinder = service as BtleService.LocalBinder
//            Log.i("imu", "Service connected")
//
//            retrieveBoard(getString(R.string.mac_address))
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//
//        }
//
//
//    }
//
//    fun startAccelerometer() {
//        accelerometer.start()
//        accelerometer.acceleration().start()
//    }
//
//    fun stopAccelerometer() {
//        accelerometer.stop()
//        accelerometer.acceleration().stop()
//    }
//
//
//    fun retrieveBoard(macAddress: String) {
//        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        val remoteDevice = btManager.adapter.getRemoteDevice(macAddress)
//        board = serviceBinder.getMetaWearBoard(remoteDevice)
//        board.connectAsync().onSuccessTask { task ->
//            Log.i("imu", "Connected to $macAddress")
//            accelerometer = board.getModule(Accelerometer::class.java)
//            accelerometer.configure().odr(25f).commit()
//
//            accelerometer.acceleration().addRouteAsync { source ->
//                source.stream { data, _ ->
//                    Log.i(
//                        "imu",
//                        "Acceleration: ${data.value(Acceleration::class.java)}"
//                    )
//                    trainingViewModel.updateAccelerationData(data.value(Acceleration::class.java))
//                }
//            }
//
//        }.continueWith { task ->
//            if (task.isFaulted) {
//                Log.i("imu", "Board failed to connect")
//            } else {
//                Log.i("imu", "Route setup completed")
//            }
//            null
//        }
//    }
//
//


}