package app.krafted.disasterready.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.disasterready.data.model.Tip
import app.krafted.disasterready.ui.theme.DarkBorder
import app.krafted.disasterready.ui.theme.TextPrimary
import app.krafted.disasterready.ui.theme.TextSecondary
import app.krafted.disasterready.ui.theme.TextTertiary

@Composable
fun TipCard(
    tip: Tip,
    accent: Color,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    index: Int
) {
    val sevColor = severityColor(tip.severity)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = accent.copy(alpha = 0.2f)),
                onClick = onBookmarkToggle
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRoundRect(
                        color = Color(0xFF111620),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )

                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = size.width * 0.4f
                        ),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.04f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = size.height * 0.3f
                        ),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )

                    drawRoundRect(
                        color = DarkBorder.copy(alpha = 0.6f),
                        cornerRadius = CornerRadius(16.dp.toPx()),
                        style = Stroke(width = 0.5.dp.toPx())
                    )
                }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(sevColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SeverityBadge(severity = tip.severity)

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isBookmarked) accent.copy(alpha = 0.12f)
                                else Color.Transparent
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, radius = 18.dp),
                                onClick = onBookmarkToggle
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark
                            else Icons.Filled.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Remove bookmark"
                            else "Add bookmark",
                            tint = if (isBookmarked) accent else TextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = tip.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    ),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = tip.body,
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 20.sp,
                        letterSpacing = 0.1.sp
                    ),
                    color = TextSecondary
                )
            }
        }
    }
}
