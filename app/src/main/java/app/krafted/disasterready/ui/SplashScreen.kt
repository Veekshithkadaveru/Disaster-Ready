package app.krafted.disasterready.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.krafted.disasterready.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    var logoScale by remember { mutableFloatStateOf(0f) }
    var logoAlpha by remember { mutableFloatStateOf(0f) }
    var glowAlpha by remember { mutableFloatStateOf(0f) }

    val scaleAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(0f) }
    val glowAnim  = remember { Animatable(0f) }

    val infinite = rememberInfiniteTransition(label = "glow_pulse")
    val pulseScale by infinite.animateFloat(
        initialValue  = 1f,
        targetValue   = 1.12f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        delay(80)

        coroutineScope {
            launch {
                alphaAnim.animateTo(
                    1f,
                    tween(500, easing = EaseOutCubic)
                ) { logoAlpha = value }
            }
            launch {
                scaleAnim.animateTo(
                    1f,
                    tween(650, easing = EaseOutBack)
                ) { logoScale = value }
            }
        }

        glowAnim.animateTo(
            1f,
            tween(500, easing = EaseOutCubic)
        ) { glowAlpha = value }

        delay(1200)
        onSplashFinished()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14))
    ) {
        Box(contentAlignment = Alignment.Center) {

            Box(
                modifier = Modifier
                    .size(190.dp)
                    .scale(if (glowAlpha >= 0.99f) pulseScale else 1f)
                    .graphicsLayer { alpha = glowAlpha }
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFF6B35).copy(alpha = 0.50f),
                                    Color(0xFFFF4500).copy(alpha = 0.18f),
                                    Color.Transparent
                                )
                            )
                        )
                    }
            )

            Image(
                painter            = painterResource(R.drawable.ic_splash_logo),
                contentDescription = "Disaster Ready",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(130.dp)
                    .graphicsLayer {
                        scaleX = logoScale
                        scaleY = logoScale
                        alpha  = logoAlpha
                    }
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            listOf(Color(0xFFFF6B35), Color(0xFFFF4500), Color(0xFFFF6B35))
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}
