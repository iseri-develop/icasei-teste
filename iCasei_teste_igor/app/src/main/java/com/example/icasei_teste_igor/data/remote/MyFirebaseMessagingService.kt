package com.example.icasei_teste_igor.data.remote

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.icasei_teste_igor.R
import com.example.icasei_teste_igor.presentation.PlayerActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Mensagem recebida: ${remoteMessage.data}")

        // Verifique se a mensagem contém dados
        val videoId = remoteMessage.data["video_id"]
        val videoTitle = remoteMessage.data["title"]
        val videoThumbnail = remoteMessage.data["thumbnail"]

        if (!videoId.isNullOrEmpty() && !videoTitle.isNullOrEmpty()) {
            // Crie um intent para abrir a PlayerActivity com os dados do vídeo
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra("video_id", videoId)
                putExtra("title", videoTitle)
                putExtra("thumbnail", videoThumbnail)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Crie a notificação
            val notification = NotificationCompat.Builder(this, "default")
                .setContentTitle("Novo Vídeo")
                .setContentText("Clique para assistir")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            // Exiba a notificação
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
    }
}
