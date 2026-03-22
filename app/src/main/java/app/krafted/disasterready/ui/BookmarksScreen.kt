package app.krafted.disasterready.ui

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.krafted.disasterready.ui.components.TipCard
import app.krafted.disasterready.ui.theme.DarkBackground
import app.krafted.disasterready.ui.theme.TextSecondary
import app.krafted.disasterready.ui.theme.TextTertiary
import app.krafted.disasterready.viewmodel.BookmarkViewModel

@Composable
fun BookmarksScreen(
    onBackClick: () -> Unit,
    viewModel: BookmarkViewModel = viewModel()
) {
    val bookmarkedChapters by viewModel.bookmarkedChapters.collectAsState()
    val totalCount = bookmarkedChapters.sumOf { it.tips.size }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        if (totalCount == 0) {
            EmptyBookmarksState()
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 40.dp),
            modifier = Modifier.navigationBarsPadding()
        ) {
            item(key = "header") {
                BookmarksHeader(
                    totalCount = totalCount,
                    onBackClick = onBackClick
                )
            }

            bookmarkedChapters.forEach { bookmarkedChapter ->
                val accent = try {
                    Color(AndroidColor.parseColor(bookmarkedChapter.accentColor))
                } catch (_: IllegalArgumentException) {
                    Color(0xFFE53935)
                }

                itemsIndexed(
                    bookmarkedChapter.tips,
                    key = { _, tip -> "${bookmarkedChapter.id}_${tip.id}" }
                ) { index, tip ->
                    TipCard(
                        tip = tip,
                        accent = accent,
                        isBookmarked = true,
                        onBookmarkToggle = { viewModel.removeBookmark(tip.id) },
                        index = index,
                        disasterLabel = bookmarkedChapter.title,
                        disasterIcon = bookmarkedChapter.icon
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarksHeader(
    totalCount: Int,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 24.dp)
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

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Saved Tips",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (totalCount == 0) "No tips saved yet"
            else "$totalCount saved tip${if (totalCount != 1) "s" else ""}",
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
    }
}

@Composable
private fun EmptyBookmarksState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.04f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.BookmarkBorder,
                    contentDescription = null,
                    tint = TextTertiary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "No saved tips yet",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bookmark tips from any disaster guide",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
    }
}
