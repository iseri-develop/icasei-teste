package com.example.icasei_teste_igor.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.icasei_teste_igor.domain.model.FavoriteVideo
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(video: FavoriteVideo)

    @Delete
    suspend fun removeFavorite(video: FavoriteVideo)

    @Query("SELECT * FROM favorite_video WHERE userId = :userId")
    fun getAllFavorites(userId: String): Flow<List<FavoriteVideo>>

    @Query("SELECT COUNT(*) > 0 FROM favorite_video WHERE videoId = :videoId AND userId = :userId")
    fun isFavorite(videoId: String, userId: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_video WHERE videoId = :videoId AND userId = :userId LIMIT 1")
    fun getFavoriteById(videoId: String, userId: String): Flow<FavoriteVideo?>

    @Query("SELECT * FROM favorite_video WHERE videoId = :videoId AND userId = :userId LIMIT 1")
    suspend fun getFavoriteByIdSync(videoId: String, userId: String): FavoriteVideo?
}
