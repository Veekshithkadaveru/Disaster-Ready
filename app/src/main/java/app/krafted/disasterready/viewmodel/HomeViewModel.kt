package app.krafted.disasterready.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import app.krafted.disasterready.data.GuideRepository
import app.krafted.disasterready.data.model.Chapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GuideRepository(application)

    val chapters: StateFlow<List<Chapter>> =
        MutableStateFlow(repository.getChapters()).asStateFlow()
}
