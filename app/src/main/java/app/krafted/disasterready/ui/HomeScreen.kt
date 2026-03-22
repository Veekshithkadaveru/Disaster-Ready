package app.krafted.disasterready.ui

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.krafted.disasterready.data.model.Chapter
import app.krafted.disasterready.ui.theme.DarkBackground
import app.krafted.disasterready.ui.theme.DarkBorder
import app.krafted.disasterready.ui.theme.DarkSurfaceHigh
import app.krafted.disasterready.ui.theme.TextPrimary
import app.krafted.disasterready.ui.theme.TextSecondary
import app.krafted.disasterready.ui.theme.TextTertiary
import app.krafted.disasterready.R
import app.krafted.disasterready.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onChapterClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val chapters by viewModel.chapters.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.35f)
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 40.dp),
            modifier = Modifier.navigationBarsPadding()
        ) {
            item {
                HomeHeader(
                    onSearchClick = onSearchClick,
                    onBookmarksClick = onBookmarksClick
                )
            }

            item {
                SectionLabel()
            }

            itemsIndexed(chapters) { index, chapter ->
                ChapterCard(
                    chapter = chapter,
                    index = index,
                    onClick = { onChapterClick(chapter.id) }
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    onSearchClick: () -> Unit,
    onBookmarksClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        DarkBackground,
                        DarkBackground.copy(alpha = 0.95f),
                        DarkBackground.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 36.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Disaster",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 44.sp,
                            letterSpacing = (-1.2).sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Ready.",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 44.sp,
                            letterSpacing = (-1.2).sp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFE53935),
                                    Color(0xFFFF7043)
                                )
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFE53935),
                                        Color(0xFFFF7043).copy(alpha = 0.4f)
                                    )
                                )
                            )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Know what to do.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp
                            ),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Stay alive.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp
                            ),
                            color = Color(0xFFE53935).copy(alpha = 0.85f)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    HeaderIconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    HeaderIconButton(onClick = onBookmarksClick) {
                        Icon(
                            imageVector = Icons.Filled.BookmarkBorder,
                            contentDescription = "Bookmarks",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 20.dp),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun SectionLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 28.dp, bottom = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE53935))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "SURVIVAL GUIDES",
            style = MaterialTheme.typography.labelMedium.copy(
                letterSpacing = 2.4.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.5.dp)
                .background(Color.White.copy(alpha = 0.08f))
        )
    }
}

@Composable
private fun ChapterCard(chapter: Chapter, index: Int, onClick: () -> Unit) {
    val offsetY = remember { Animatable(40f) }
    val cardAlpha = remember { Animatable(0f) }
    val accentProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(index * 80L)
        kotlinx.coroutines.coroutineScope {
            launch {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                cardAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300)
                )
            }
            launch {
                accentProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 400)
                )
            }
        }
    }

    val accent = remember(chapter.accentColor) {
        try {
            Color(AndroidColor.parseColor(chapter.accentColor))
        } catch (_: IllegalArgumentException) {
            Color(0xFFE53935)
        }
    }

    val context = LocalContext.current
    val iconResId = remember(chapter.icon) {
        context.resources.getIdentifier(chapter.icon, "drawable", context.packageName)
    }
    val backgroundResId = remember(chapter.background) {
        context.resources.getIdentifier(chapter.background, "drawable", context.packageName)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .graphicsLayer {
                translationY = offsetY.value
                alpha = cardAlpha.value
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = true,
                    color = accent.copy(alpha = 0.3f)
                ),
                onClick = onClick
            )
    ) {
        Box(modifier = Modifier.matchParentSize().background(Color(0xFF111620)))

        if (backgroundResId != 0) {
            Image(
                painter = painterResource(id = backgroundResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.7f)
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF111620),
                                Color(0xFF111620).copy(alpha = 0.7f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = size.width * 0.85f
                        ),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )

                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.15f),
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
                                Color.White.copy(alpha = 0.05f),
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
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 0.5.dp.toPx()
                        )
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
                    .graphicsLayer {
                        scaleY = accentProgress.value
                        alpha = accentProgress.value
                    }
                    .background(accent)
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp, top = 18.dp, bottom = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = chapter.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(accent.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${chapter.tips.size} tips",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.4.sp
                            ),
                            color = accent
                        )
                    }
                }

                if (iconResId != 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = chapter.title,
                            modifier = Modifier.size(56.dp),
                            contentScale = ContentScale.Fit,
                            alpha = 1f
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = TextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
