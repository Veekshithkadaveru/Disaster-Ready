package app.krafted.disasterready.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.disasterready.data.GuideRepository
import app.krafted.disasterready.data.db.AppDatabase
import app.krafted.disasterready.data.model.Tip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookmarkedChapter(
    val id: String,
    val title: String,
    val accentColor: String,
    val icon: String,
    val tips: List<Tip>
)

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GuideRepository(application)
    private val bookmarkDao = AppDatabase.getInstance(application).bookmarkDao()

    private var chaptersById: Map<String, app.krafted.disasterready.data.model.Chapter> = emptyMap()
    private var allTipsById: Map<Int, Tip> = emptyMap()

    private val _dataReady = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.loadChapters()
            chaptersById = repository.getChapters().associateBy { it.id }
            allTipsById = repository.getAllTips().associateBy { it.id }
            _dataReady.value = true
        }
    }

    val bookmarkedChapters: StateFlow<List<BookmarkedChapter>> =
        combine(
            bookmarkDao.getAllBookmarks(),
            _dataReady
        ) { bookmarks, ready ->
            if (!ready) return@combine emptyList()
            bookmarks
                .groupBy { it.chapterId }
                .mapNotNull { (chapterId, entities) ->
                    val chapter = chaptersById[chapterId] ?: return@mapNotNull null
                    val tips = entities.mapNotNull { entity -> allTipsById[entity.tipId] }
                    if (tips.isEmpty()) return@mapNotNull null
                    BookmarkedChapter(
                        id = chapter.id,
                        title = chapter.title,
                        accentColor = chapter.accentColor,
                        icon = chapter.icon,
                        tips = tips
                    )
                }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeBookmark(tipId: Int) {
        viewModelScope.launch {
            bookmarkDao.removeBookmark(tipId)
        }
    }
}

