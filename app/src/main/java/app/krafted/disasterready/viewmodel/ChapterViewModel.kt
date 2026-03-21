package app.krafted.disasterready.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.disasterready.data.GuideRepository
import app.krafted.disasterready.data.db.AppDatabase
import app.krafted.disasterready.data.db.BookmarkEntity
import app.krafted.disasterready.data.model.Chapter
import app.krafted.disasterready.data.model.Phase
import app.krafted.disasterready.data.model.Tip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChapterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GuideRepository(application)
    private val bookmarkDao = AppDatabase.getInstance(application).bookmarkDao()

    private val _chapterId = MutableStateFlow("")
    private val _activePhase = MutableStateFlow(Phase.BEFORE)
    val activePhase: StateFlow<Phase> = _activePhase.asStateFlow()

    private var _chapter: Chapter? = null
    val chapter: StateFlow<Chapter?> get() = _chapterState
    private val _chapterState = MutableStateFlow<Chapter?>(null)

    val filteredTips: StateFlow<List<Tip>> = combine(
        _chapterState,
        _activePhase
    ) { chapter, phase ->
        chapter?.tips?.filter { it.phase == phase } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedTipIds: StateFlow<Set<Int>> = bookmarkDao.getBookmarkedIds()
        .combine(MutableStateFlow(Unit)) { ids, _ -> ids.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun loadChapter(chapterId: String) {
        if (_chapterId.value == chapterId) return
        _chapterId.value = chapterId
        viewModelScope.launch {
            repository.loadChapters()
            _chapter = repository.getChapter(chapterId)
            _chapterState.value = _chapter
        }
    }

    fun setActivePhase(phase: Phase) {
        _activePhase.value = phase
    }

    fun toggleBookmark(tip: Tip) {
        viewModelScope.launch {
            val chapterId = _chapterId.value
            if (bookmarkedTipIds.value.contains(tip.id)) {
                bookmarkDao.removeBookmark(tip.id)
            } else {
                bookmarkDao.addBookmark(
                    BookmarkEntity(
                        tipId = tip.id,
                        chapterId = chapterId,
                        tipTitle = tip.title
                    )
                )
            }
        }
    }
}
