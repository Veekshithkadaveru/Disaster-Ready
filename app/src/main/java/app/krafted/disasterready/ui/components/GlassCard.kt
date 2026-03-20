package app.krafted.disasterready.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.krafted.disasterready.ui.theme.DarkBorderSubtle
import app.krafted.disasterready.ui.theme.DarkSurfaceHigh

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurfaceHigh.copy(alpha = 0.78f)
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = DarkBorderSubtle
        )
    ) {
        content()
    }
}
