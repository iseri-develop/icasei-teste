package com.example.icasei_teste_igor.data.local

import androidx.room.*
import com.example.icasei_teste_igor.domain.model.Playlist
import com.example.icasei_teste_igor.domain.model.PlaylistVideo
import com.example.icasei_teste_igor.domain.model.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    // Inserir uma nova playlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)

    // Obter todas as playlists de um usuário específico
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    fun getAllPlaylists(userId: String): Flow<List<Playlist>>

    // Remover uma playlist pelo ID
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)

    // Inserir um vídeo no banco de dados (caso ainda não exista)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideo(video: Video)

    // Associar um vídeo a uma playlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistVideo(playlistVideo: PlaylistVideo)

    // Remover um vídeo de uma playlist
    @Query("DELETE FROM playlist_video WHERE playlistId = :playlistId AND videoId = :videoId")
    suspend fun removeVideoFromPlaylist(playlistId: Long, videoId: String)

    // Buscar vídeos de uma playlist específica
    @Transaction
    @Query("""
        SELECT v.* FROM videos v 
        INNER JOIN playlist_video pv ON v.videoId = pv.videoId 
        WHERE pv.playlistId = :playlistId
    """)
    suspend fun getVideosForPlaylist(playlistId: Long): List<Video>
}
