package app.krafted.disasterready.data

import android.content.Context
import app.krafted.disasterready.data.model.Chapter
import app.krafted.disasterready.data.model.Tip
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

private data class Guide(
    @SerializedName("chapters") val chapters: List<Chapter>
)

class GuideRepository(context: Context) {

    private val chapters: List<Chapter> by lazy {
        val json = context.assets.open("guide.json").bufferedReader().use { it.readText() }
        Gson().fromJson(json, Guide::class.java).chapters
    }

    fun getChapters(): List<Chapter> = chapters

    fun getChapter(id: String): Chapter? = chapters.find { it.id == id }

    fun getAllTips(): List<Tip> = chapters.flatMap { it.tips }
}
