package app.krafted.disasterready.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE tipId = :tipId")
    suspend fun removeBookmark(tipId: Int)

    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT tipId FROM bookmarks")
    fun getBookmarkedIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE tipId = :tipId)")
    fun isBookmarked(tipId: Int): Flow<Boolean>
}
