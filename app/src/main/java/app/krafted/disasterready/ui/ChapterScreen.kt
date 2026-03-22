package app.krafted.disasterready.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.krafted.disasterready.R
import app.krafted.disasterready.data.model.Chapter
import app.krafted.disasterready.data.model.Phase
import app.krafted.disasterready.ui.components.TipCard
import app.krafted.disasterready.ui.theme.DarkBackground
import app.krafted.disasterready.ui.theme.DarkBorder
import app.krafted.disasterready.ui.theme.DarkSurfaceHigh
import app.krafted.disasterready.ui.theme.TextPrimary
import app.krafted.disasterready.ui.theme.TextTertiary
import app.krafted.disasterready.viewmodel.ChapterViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.graphics.Color as AndroidColor

@Composable
fun ChapterScreen(
    chapterId: String,
    onBackClick: () -> Unit,
    highlightTipId: Int? = null,
    viewModel: ChapterViewModel = viewModel()
) {
    LaunchedEffect(chapterId, highlightTipId) {
        viewModel.loadChapter(chapterId, highlightTipId)
    }

    val chapter by viewModel.chapter.collectAsState()
    val activePhase by viewModel.activePhase.collectAsState()
    val filteredTips by viewModel.filteredTips.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedTipIds.collectAsState()
    val currentHighlightId by viewModel.highlightTipId.collectAsState()

    val chapterData = chapter ?: return

    val accent = remember(chapterData.accentColor) {
        try {
            Color(AndroidColor.parseColor(chapterData.accentColor))
        } catch (_: IllegalArgumentException) {
            Color(0xFFE53935)
        }
    }

    val context = LocalContext.current
    val backgroundResId = remember(chapterData.background) {
        context.resources.getIdentifier(chapterData.background, "drawable", context.packageName)
    }
    val iconResId = remember(chapterData.icon) {
        context.resources.getIdentifier(chapterData.icon, "drawable", context.packageName)
    }

    val listState = rememberLazyListState()

    val parallaxOffset = remember(listState) {
        androidx.compose.runtime.derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset * 0.4f
            } else {
                200f
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        if (backgroundResId != 0) {
            Image(
                painter = painterResource(id = backgroundResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = -parallaxOffset.value
                    }
                    .alpha(0.45f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to DarkBackground.copy(alpha = 0.5f),
                            0.15f to DarkBackground.copy(alpha = 0.35f),
                            0.4f to DarkBackground.copy(alpha = 0.55f),
                            0.65f to DarkBackground.copy(alpha = 0.85f),
                            1.0f to DarkBackground.copy(alpha = 0.97f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accent.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        radius = 800f
                    )
                )
        )

        LaunchedEffect(currentHighlightId, filteredTips) {
            if (currentHighlightId != null && filteredTips.isNotEmpty()) {
                val tipIndex = filteredTips.indexOfFirst { it.id == currentHighlightId }
                if (tipIndex >= 0) {
                    listState.animateScrollToItem(tipIndex + 4)
                }
            }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 40.dp),
            modifier = Modifier.navigationBarsPadding()
        ) {
            item {
                ChapterHeader(
                    chapter = chapterData,
                    accent = accent,
                    iconResId = iconResId,
                    onBackClick = onBackClick
                )
            }

            item {
                QuickFactCard(
                    quickFact = chapterData.quickFact,
                    accent = accent
                )
            }

            item {
                SectionLabel(
                    text = stringResource(R.string.chapter_section_survival_tips),
                    accent = accent
                )
            }

            item {
                PhaseTabs(
                    activePhase = activePhase,
                    accent = accent,
                    onPhaseSelected = { viewModel.setActivePhase(it) }
                )
            }

            itemsIndexed(filteredTips, key = { _, tip -> tip.id }) { index, tip ->
                val tipOffsetY = remember { Animatable(30f) }
                val tipAlpha = remember { Animatable(0f) }

                LaunchedEffect(tip.id, activePhase) {
                    tipOffsetY.snapTo(30f)
                    tipAlpha.snapTo(0f)
                    delay(index * 60L)
                    coroutineScope {
                        launch {
                            tipOffsetY.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                        launch {
                            tipAlpha.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    }
                }

                val isHighlighted = tip.id == currentHighlightId
                var highlightShown by remember(tip.id) { mutableStateOf(isHighlighted) }

                if (isHighlighted) {
                    LaunchedEffect(Unit) {
                        delay(2500)
                        highlightShown = false
                        viewModel.clearHighlight()
                    }
                }

                val highlightAlpha by animateFloatAsState(
                    targetValue = if (highlightShown) 1f else 0f,
                    animationSpec = tween(if (highlightShown) 400 else 800),
                    label = "highlight"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = tipOffsetY.value
                            alpha = tipAlpha.value
                        }
                        .then(
                            if (highlightAlpha > 0f) {
                                Modifier.drawBehind {
                                    drawRoundRect(
                                        color = accent.copy(alpha = 0.10f * highlightAlpha),
                                        cornerRadius = CornerRadius(16.dp.toPx())
                                    )
                                }
                            } else Modifier
                        )
                ) {
                    TipCard(
                        tip = tip,
                        accent = accent,
                        isBookmarked = bookmarkedIds.contains(tip.id),
                        onBookmarkToggle = { viewModel.toggleBookmark(tip) },
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
private fun ChapterHeader(
    chapter: Chapter,
    accent: Color,
    iconResId: Int,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground.copy(alpha = 0.6f),
                        DarkBackground.copy(alpha = 0.4f),
                        DarkBackground.copy(alpha = 0.15f),
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
                .padding(top = 16.dp, bottom = 28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, radius = 20.dp),
                        onClick = onBackClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.chapter_back),
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 42.sp,
                            letterSpacing = (-1.2).sp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    accent.copy(alpha = 0.9f)
                                )
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        accent,
                                        accent.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = chapter.subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.3.sp
                        ),
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(accent.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.chapter_tips_count, chapter.tips.size),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.4.sp
                            ),
                            color = accent
                        )
                    }
                }

                if (iconResId != 0) {
                    val infiniteTransition = rememberInfiniteTransition(label = "symbolRotation")
                    val rotation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 20000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "rotation"
                    )

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .drawBehind {
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                accent.copy(alpha = 0.12f),
                                                Color.Transparent
                                            )
                                        ),
                                        radius = size.minDimension / 2
                                    )
                                }
                        )
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = chapter.title,
                            modifier = Modifier
                                .size(64.dp)
                                .graphicsLayer { rotationZ = rotation },
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickFactCard(quickFact: String, accent: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRoundRect(
                        color = DarkSurfaceHigh.copy(alpha = 0.78f),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )

                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = size.width * 0.5f
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
                            endY = size.height * 0.4f
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
                    .background(accent)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.chapter_quick_fact),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.4.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = accent
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = quickFact,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp
                    ),
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, accent: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(accent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
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
private fun PhaseTabs(
    activePhase: Phase,
    accent: Color,
    onPhaseSelected: (Phase) -> Unit
) {
    val phases = Phase.entries

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        phases.forEach { phase ->
            val isSelected = phase == activePhase

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (isSelected) {
                            Modifier.drawBehind {
                                drawRoundRect(
                                    color = accent.copy(alpha = 0.15f),
                                    cornerRadius = CornerRadius(12.dp.toPx())
                                )
                                drawRoundRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.05f),
                                            Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = size.height * 0.5f
                                    ),
                                    cornerRadius = CornerRadius(12.dp.toPx())
                                )
                                drawRoundRect(
                                    color = accent.copy(alpha = 0.4f),
                                    cornerRadius = CornerRadius(12.dp.toPx()),
                                    style = Stroke(width = 0.5.dp.toPx())
                                )
                            }
                        } else {
                            Modifier.drawBehind {
                                drawRoundRect(
                                    color = DarkSurfaceHigh.copy(alpha = 0.5f),
                                    cornerRadius = CornerRadius(12.dp.toPx())
                                )
                                drawRoundRect(
                                    color = DarkBorder.copy(alpha = 0.3f),
                                    cornerRadius = CornerRadius(12.dp.toPx()),
                                    style = Stroke(width = 0.5.dp.toPx())
                                )
                            }
                        }
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = accent.copy(alpha = 0.3f)),
                        onClick = { onPhaseSelected(phase) }
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = phase.name,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 1.6.sp
                    ),
                    color = if (isSelected) accent else TextTertiary
                )
            }
        }
    }
}