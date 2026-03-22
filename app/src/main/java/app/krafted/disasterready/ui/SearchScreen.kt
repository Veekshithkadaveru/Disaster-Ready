package app.krafted.disasterready.ui

import android.graphics.Color as AndroidColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.krafted.disasterready.ui.components.SeverityBadge
import app.krafted.disasterready.ui.components.severityColor
import app.krafted.disasterready.ui.theme.DarkBackground
import app.krafted.disasterready.ui.theme.DarkBorder
import app.krafted.disasterready.ui.theme.TextPrimary
import app.krafted.disasterready.ui.theme.TextSecondary
import app.krafted.disasterready.ui.theme.TextTertiary
import app.krafted.disasterready.viewmodel.SearchResult
import app.krafted.disasterready.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onResultClick: (chapterId: String, tipId: Int) -> Unit = { _, _ -> },
    viewModel: SearchViewModel = viewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 40.dp),
            modifier = Modifier.navigationBarsPadding()
        ) {
            item {
                SearchHeader(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    onBackClick = onBackClick,
                    focusRequester = focusRequester
                )
            }


            if (query.isNotBlank() && results.isEmpty()) {
                item {
                    EmptySearchState(query = query)
                }
            }


            if (query.isNotBlank() && results.isNotEmpty()) {
                item {
                    ResultCountBar(count = results.size)
                }
            }

            itemsIndexed(results, key = { _, r -> r.tip.id }) { index, result ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(200)) +
                            slideInVertically(
                                initialOffsetY = { it / 4 },
                                animationSpec = tween(280, delayMillis = index.coerceAtMost(8) * 40)
                            )
                ) {
                    SearchResultCard(
                        result = result,
                        query = query,
                        onClick = { onResultClick(result.chapterId, result.tip.id) }
                    )
                }
            }
        }
    }
}


@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 8.dp)
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
                contentDescription = "Back",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Find tips across all disaster guides",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.3.sp
            ),
            color = TextSecondary
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
                            Color(0xFFE53935),
                            Color(0xFFE53935).copy(alpha = 0.3f)
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(20.dp))

        SearchField(
            query = query,
            onQueryChange = onQueryChange,
            focusRequester = focusRequester
        )
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val glowAlpha by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0f,
        animationSpec = tween(300),
        label = "glow"
    )
    val accentRed = Color(0xFFE53935)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .drawBehind {
                if (glowAlpha > 0f) {
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                accentRed.copy(alpha = 0.10f * glowAlpha),
                                accentRed.copy(alpha = 0.03f * glowAlpha)
                            )
                        ),
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                }
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.05f),
                    cornerRadius = CornerRadius(14.dp.toPx())
                )
                val borderColor = if (glowAlpha > 0.5f)
                    accentRed.copy(alpha = 0.30f)
                else
                    DarkBorder.copy(alpha = 0.6f)
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(14.dp.toPx()),
                    style = Stroke(width = (0.5f + 0.5f * glowAlpha).dp.toPx())
                )
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = if (isFocused) accentRed.copy(alpha = 0.7f) else TextTertiary,
                modifier = Modifier.size(20.dp)
            )

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(accentRed),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search tips, disasters...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = TextTertiary
                            )
                        }
                        innerTextField()
                    }
                }
            )

            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(tween(150)),
                exit = fadeOut(tween(150))
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, radius = 13.dp),
                            onClick = { onQueryChange("") }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear",
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}




@Composable
private fun ResultCountBar(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = Color(0xFFE53935)
            )
        }

        Text(
            text = "  result${if (count != 1) "s" else ""} found",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            ),
            color = TextTertiary
        )
    }
}


@Composable
private fun EmptySearchState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(color = Color.White.copy(alpha = 0.03f))
                    drawCircle(
                        color = Color.White.copy(alpha = 0.06f),
                        radius = size.minDimension / 2 * 0.7f
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = TextTertiary.copy(alpha = 0.5f),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No results found",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Nothing matched \"$query\"",
            style = MaterialTheme.typography.bodySmall.copy(
                letterSpacing = 0.2.sp
            ),
            color = TextTertiary
        )
    }
}


@Composable
private fun SearchResultCard(
    result: SearchResult,
    query: String,
    onClick: () -> Unit
) {
    val accent = try {
        Color(AndroidColor.parseColor(result.accentColor))
    } catch (_: IllegalArgumentException) {
        Color(0xFFE53935)
    }
    val sevColor = severityColor(result.tip.severity)
    val context = LocalContext.current
    val iconResId = remember(result.icon) {
        context.resources.getIdentifier(result.icon, "drawable", context.packageName)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = accent.copy(alpha = 0.2f)),
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRoundRect(
                        color = Color(0xFF111620),
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.06f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = size.width * 0.5f
                        ),
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.03f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = size.height * 0.35f
                        ),
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                    drawRoundRect(
                        color = DarkBorder.copy(alpha = 0.5f),
                        cornerRadius = CornerRadius(14.dp.toPx()),
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
                    .width(3.dp)
                    .fillMaxHeight()
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(accent, sevColor)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (iconResId != 0) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .drawBehind {
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    accent.copy(alpha = 0.15f),
                                                    Color.Transparent
                                                )
                                            ),
                                            radius = size.minDimension / 2
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = iconResId),
                                    contentDescription = result.chapterTitle,
                                    modifier = Modifier.size(14.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        Text(
                            text = result.chapterTitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp,
                                fontSize = 10.sp
                            ),
                            color = accent
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = result.tip.phase.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.6.sp,
                                fontSize = 8.sp
                            ),
                            color = TextTertiary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    accent.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SeverityBadge(severity = result.tip.severity)
                }

                Spacer(modifier = Modifier.height(8.dp))

                HighlightedText(
                    text = result.tip.title,
                    highlight = query,
                    accentColor = accent,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    ),
                    baseColor = TextPrimary
                )

                Spacer(modifier = Modifier.height(5.dp))

                HighlightedText(
                    text = result.tip.body,
                    highlight = query,
                    accentColor = accent,
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.1.sp
                    ),
                    baseColor = TextSecondary,
                    maxLines = 2
                )
            }
        }
    }
}


@Composable
private fun HighlightedText(
    text: String,
    highlight: String,
    accentColor: Color,
    style: androidx.compose.ui.text.TextStyle,
    baseColor: Color,
    maxLines: Int = Int.MAX_VALUE
) {
    if (highlight.isBlank()) {
        Text(
            text = text,
            style = style,
            color = baseColor,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
        return
    }

    val annotated = buildAnnotatedString {
        val lower = text.lowercase()
        val hlLower = highlight.trim().lowercase()
        var start = 0
        while (start < text.length) {
            val idx = lower.indexOf(hlLower, start)
            if (idx == -1) {
                withStyle(SpanStyle(color = baseColor)) {
                    append(text.substring(start))
                }
                break
            }
            if (idx > start) {
                withStyle(SpanStyle(color = baseColor)) {
                    append(text.substring(start, idx))
                }
            }
            withStyle(
                SpanStyle(
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    background = accentColor.copy(alpha = 0.10f)
                )
            ) {
                append(text.substring(idx, idx + hlLower.length))
            }
            start = idx + hlLower.length
        }
    }

    Text(
        text = annotated,
        style = style,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}
