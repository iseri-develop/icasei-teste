package com.example.icasei_teste_igor.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.icasei_teste_igor.domain.model.FavoriteVideo
import com.example.icasei_teste_igor.domain.model.Playlist
import com.example.icasei_teste_igor.domain.model.PlaylistVideo
import com.example.icasei_teste_igor.domain.model.Video

@Database(
    entities = [FavoriteVideo::class, Playlist::class, PlaylistVideo::class, Video::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVideoDao(): FavoriteVideoDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "icasei_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

