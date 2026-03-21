package app.krafted.disasterready.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.disasterready.data.GuideRepository
import app.krafted.disasterready.data.model.Tip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SearchResult(
    val tip: Tip,
    val chapterTitle: String,
    val chapterId: String,
    val accentColor: String,
    val icon: String
)

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GuideRepository(application)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _dataReady = MutableStateFlow(false)
    private var allResults: List<SearchResult> = emptyList()

    init {
        viewModelScope.launch {
            repository.loadChapters()
            val chapters = repository.getChapters()
            allResults = chapters.flatMap { chapter ->
                chapter.tips.map { tip ->
                    SearchResult(
                        tip = tip,
                        chapterTitle = chapter.title,
                        chapterId = chapter.id,
                        accentColor = chapter.accentColor,
                        icon = chapter.icon
                    )
                }
            }
            _dataReady.value = true
        }
    }

    val results: StateFlow<List<SearchResult>> =
        combine(_query, _dataReady) { q, ready ->
            if (!ready || q.isBlank()) return@combine emptyList()
            val lower = q.trim().lowercase()
            allResults.filter { result ->
                result.tip.title.lowercase().contains(lower) ||
                        result.tip.body.lowercase().contains(lower)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}
