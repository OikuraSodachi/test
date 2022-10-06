package com.todokanai.buildeight.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.todokanai.buildeight.R
import com.todokanai.buildeight.TrackTool
import com.todokanai.buildeight.receiver.TrackBroadcastReceiver

class ForegroundPlayService : Service() {

    val CHANNEL_ID = "ForegroundPlayServiceChannel"
    companion object {
        val ACTION_PREV = "prev"        // ACTION 종류 선언
        val ACTION_NEXT = "next"
        val ACTION_PAUSE_PLAY = "pauseplay"
        val ACTION_CLOSE = "close"
        val ACTION_REPLAY = "replay"
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }

    fun createNotificationChannel() {                           // 서비스 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_NONE             //  알림의 중요도
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {   // 서비스 본문?

        var mediaPlayer:MediaPlayer? = null

        registerReceiver(TrackBroadcastReceiver(), IntentFilter(ACTION_REPLAY))
        registerReceiver(TrackBroadcastReceiver(), IntentFilter(ACTION_PREV))
        registerReceiver(TrackBroadcastReceiver(), IntentFilter(ACTION_PAUSE_PLAY))
        registerReceiver(TrackBroadcastReceiver(), IntentFilter(ACTION_NEXT))
        registerReceiver(TrackBroadcastReceiver(), IntentFilter(ACTION_CLOSE))
        //------------------------------------------

        val repeatIntent = PendingIntent.getBroadcast(this, 0 , Intent(ACTION_REPLAY),PendingIntent.FLAG_IMMUTABLE)
        val prevIntent = PendingIntent.getBroadcast(this, 0 , Intent(ACTION_PREV),PendingIntent.FLAG_IMMUTABLE)
        val pauseplayIntent = PendingIntent.getBroadcast(this, 0 , Intent(ACTION_PAUSE_PLAY),PendingIntent.FLAG_IMMUTABLE)
        val nextIntent = PendingIntent.getBroadcast(this, 0 , Intent(ACTION_NEXT),PendingIntent.FLAG_IMMUTABLE)
        val closeIntent = PendingIntent.getBroadcast(this, 0 , Intent(ACTION_CLOSE),PendingIntent.FLAG_IMMUTABLE)

        Log.d("tester","receiver registered")
        //------------------------------------------------
        val action = intent?.action     //ACTION의 내용
        when(action) {
            ACTION_REPLAY -> TrackTool(this,null).replay()
            ACTION_PREV -> TrackTool(this,null).prev()
            ACTION_PAUSE_PLAY -> TrackTool(this,null).pauseplay()
            ACTION_NEXT -> TrackTool(this,null).next()
            ACTION_CLOSE -> TrackTool(this,null).close()
        }

        createNotificationChannel() // 채널 지정
        val mediaSession = MediaSessionCompat(this,"MediaNotification")
        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE,"Song Title")
                .putString(MediaMetadata.METADATA_KEY_ARTIST,"Song Artist")
                .build()
        )

        val builder = NotificationCompat.Builder(this,CHANNEL_ID)       // 알림바에 띄울 알림을 만듬
            .setContentTitle("Foreground Play Service") // 알림의 제목
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .addAction(NotificationCompat.Action(R.drawable.ic_baseline_repeat_24,"REPEAT",repeatIntent))
            .addAction(NotificationCompat.Action(R.drawable.ic_baseline_skip_previous_24,"PREV",prevIntent))
            .addAction(NotificationCompat.Action(R.drawable.ic_baseline_pause_24,"pauseplay",pauseplayIntent))
            .addAction(NotificationCompat.Action(R.drawable.ic_baseline_skip_next_24,"NEXT",nextIntent))
            .addAction(NotificationCompat.Action(R.drawable.ic_baseline_close_24,"CLOSE",closeIntent))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1,2,3)     // 확장하지 않은상태 알림에서 쓸 기능의 배열번호
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(closeIntent)
            )
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        startForeground(1, builder)              // 지정된 알림을 실행
        return super.onStartCommand(intent, flags, startId)
    }
}
// fun close() 실행으로 종료후 계속 mediastyle 알림이 계속 살아남