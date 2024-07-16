package at.bachelor.workoutcounter.providers.date

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.util.Date

class DatePreviewProvider : PreviewParameterProvider<Date> {
    override val values = sequenceOf(
        Date(),
        Date(System.currentTimeMillis() - 86400000),
        Date(System.currentTimeMillis() + 86400000)
    )
}