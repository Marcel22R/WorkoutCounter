package at.bachelor.workoutcounter.screens.screens.homeScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import at.bachelor.workoutcounter.providers.date.DatePreviewProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

@Preview
@Composable
fun HeadlineWithDate(
    @PreviewParameter(DatePreviewProvider::class) date: Date
) {
    val formattedDate = formatter.format(date)
    Text(text = "Last workout: $formattedDate", color = Color.White)
}
