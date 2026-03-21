package app.krafted.disasterready

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.disasterready.ui.ChapterScreen
import app.krafted.disasterready.ui.HomeScreen
import app.krafted.disasterready.ui.theme.DisasterReadyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisasterReadyTheme {
                DisasterReadyApp()
            }
        }
    }
}

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val CHAPTER = "chapter/{chapterId}"
    const val BOOKMARKS = "bookmarks"
    const val SEARCH = "search"

    fun chapter(chapterId: String) = "chapter/$chapterId"
}

@Composable
fun DisasterReadyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(280)
            ) + fadeIn(animationSpec = tween(280))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(280)
            ) + fadeOut(animationSpec = tween(280))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(280)
            ) + fadeIn(animationSpec = tween(280))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(280)
            ) + fadeOut(animationSpec = tween(280))
        }
    ) {
        composable(Routes.SPLASH) {
            LaunchedEffect(Unit) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0A0D14))
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onChapterClick = { chapterId ->
                    navController.navigate(Routes.chapter(chapterId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SEARCH)
                },
                onBookmarksClick = {
                    navController.navigate(Routes.BOOKMARKS)
                }
            )
        }

        composable(
            route = Routes.CHAPTER,
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            ChapterScreen(
                chapterId = chapterId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.BOOKMARKS) {
            PlaceholderScreen("Bookmarks") {}
        }

        composable(Routes.SEARCH) {
            PlaceholderScreen("Search") {}
        }
    }
}

@Composable
private fun PlaceholderScreen(label: String, onAuto: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}
