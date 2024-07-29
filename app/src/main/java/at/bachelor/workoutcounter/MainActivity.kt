package at.bachelor.workoutcounter

import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mbientlab.metawear.MetaWearBoard
import com.mbientlab.metawear.android.BtleService


class MainActivity : ComponentActivity() {

    private lateinit var serviceBinder: BtleService.LocalBinder
    private lateinit var board: MetaWearBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Call super.onCreate() first
        setContent {
            MainCompose()
        }
        val intent = Intent(this, BtleService::class.java)
        applicationContext.bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = service as BtleService.LocalBinder
            Log.i("imu", "Service connected")

            retrieveBoard(getString(R.string.mac_address))

        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }


    }

    fun retrieveBoard(macAddress: String) {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val remoteDevice = btManager.adapter.getRemoteDevice(macAddress)
        board = serviceBinder.getMetaWearBoard(remoteDevice)
        board.connectAsync().continueWith { task ->
            if (task.isFaulted) {
                Log.i("imu", "Board failed to connect")
            } else {
                Log.i("imu", "Connected to $macAddress")
            }
            null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unbindService(serviceConnection)
    }


}