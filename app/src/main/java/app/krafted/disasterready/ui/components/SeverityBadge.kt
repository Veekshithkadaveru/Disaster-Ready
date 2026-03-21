package app.krafted.disasterready.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.disasterready.data.model.Severity

fun severityColor(severity: Severity): Color = when (severity) {
    Severity.CRITICAL -> Color(0xFFC62828)
    Severity.HIGH -> Color(0xFFE65100)
    Severity.MEDIUM -> Color(0xFFF9A825)
}

@Composable
fun SeverityBadge(severity: Severity) {
    val color = severityColor(severity)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = severity.name,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                fontSize = 10.sp
            ),
            color = color
        )
    }
}
