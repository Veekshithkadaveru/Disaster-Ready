package app.krafted.disasterready.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val tipId: Int,
    val chapterId: String,
    val tipTitle: String,
    val savedAt: Long = System.currentTimeMillis()
)
