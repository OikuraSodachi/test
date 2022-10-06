package com.todokanai.buildeight.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.todokanai.buildeight.TrackTool
import com.todokanai.buildeight.databinding.ActivitySettingsBinding
import com.todokanai.buildeight.room.RoomHelper
import com.todokanai.buildeight.service.ForegroundPlayService.Companion.mediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    lateinit var helper: RoomHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("tested111","onCreate")
        helper = RoomHelper.getInstance(applicationContext)!!

        fun scan() {
            mediaPlayer?.release()      // 혹시 몰라서 정지명령 내려둠
            val scannedList = TrackTool(applicationContext,null).scanTrackList()
            lifecycleScope.launch(Dispatchers.IO) {
                helper = RoomHelper.getInstance(applicationContext)!!

                val size = scannedList.size
                helper.roomTrackDao().deleteAll()
                for (a in 1..size) {
                    helper.roomTrackDao().insert(scannedList[a - 1])
                }                               // 스캔된 목록
                Log.d("tested111", "Scan")

            }
        }           // 음원파일 스캔 함수
        binding.Scanbtn.setOnClickListener{scan()}          // scan버튼 난타하면 리스트 사이즈 증가 issue. 작업 완료시까지 버튼 disable로 해결?

        lifecycleScope.launch(Dispatchers.IO) {
            helper = RoomHelper.getInstance(applicationContext)!!
            val playlist = helper.roomTrackDao().getAll()       // 재생목록 playlist
            val uriList = helper.roomTrackDao().getAll()
            Log.d("tested111", "$playlist")
            Log.d("tested111", "Size:${playlist.size}")
        }  // 로그작성을 위한부분. 삭제해도 무방

        val intentmain = Intent(this,MainActivity::class.java)
        binding.Backbtn.setOnClickListener {startActivity(intentmain);Log.d("tested111","back")} //Backbtn에 대한 동작
    }
    //onCreate 끝
}