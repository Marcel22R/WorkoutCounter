package at.bachelor.workoutcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import java.util.Date
import androidx.activity.compose.setContent
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            HeadlineWithDate(Date.from(Instant.now()))
        }
    }




    @Composable
    fun HeadlineWithDate(date: Date) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
        Text(text = "Last workout: $formattedDate", color=Color.White)
    }


}