package app.krafted.disasterready.data

import android.content.Context
import app.krafted.disasterready.data.model.Chapter
import app.krafted.disasterready.data.model.Tip
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

private data class Guide(
    @SerializedName("chapters") val chapters: List<Chapter>
)

class GuideRepository(private val context: Context) {

    private var _chapters: List<Chapter> = emptyList()
    private val mutex = Mutex()
    private var loaded = false

    suspend fun loadChapters(): List<Chapter> {
        if (loaded) return _chapters
        mutex.withLock {
            if (loaded) return _chapters
            _chapters = withContext(Dispatchers.IO) {
                val json = context.assets.open("guide.json").bufferedReader().use { it.readText() }
                Gson().fromJson(json, Guide::class.java).chapters
            }
            loaded = true
        }
        return _chapters
    }

    fun getChapters(): List<Chapter> = _chapters

    fun getChapter(id: String): Chapter? = _chapters.find { it.id == id }

    fun getAllTips(): List<Tip> = _chapters.flatMap { it.tips }
}
