package com.todokanai.buildeight.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.android.material.tabs.TabLayoutMediator
import com.todokanai.buildeight.adapter.FragmentAdapter
import com.todokanai.buildeight.databinding.ActivityMainBinding
import com.todokanai.buildeight.fragment.PlayingFragment
import com.todokanai.buildeight.fragment.TrackFragment
import com.todokanai.buildeight.room.RoomHelper
import com.todokanai.buildeight.service.ForegroundPlayService
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    lateinit var activityResult: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var rThread = Thread(WorkerRunnable())
        rThread.start()
        Thread {}.start()
        thread(start=true) {}


        binding.trackPager.isUserInputEnabled = false
        activityResult =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startProcess()
                } else {
                    finish()
                }
            }
        activityResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        //---------Manifest에 정의된 권한 실행?

        val intentService = Intent(this, ForegroundPlayService::class.java)
        ContextCompat.startForegroundService(this, intentService)    //----- 서비스 개시

        fun exit(){
            finishAffinity()
            stopService(intentService)      // 서비스 종료
            System.runFinalization() // 현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
            exitProcess(0)     // 현재 액티비티를 종료시킨다.
        }
        binding.Exitbtn.setOnClickListener { exit()}      //----Exitbtn에 대한 동작

            val intentSetting = Intent(this, SettingsActivity::class.java)
            binding.Settingsbtn.setOnClickListener { startActivity(intentSetting) }     //Settingsbtn에 대한 동작

            //---------탭 넘기기 관련 코드
            val fragmentList = listOf(TrackFragment(), PlayingFragment())
            val adapter = FragmentAdapter(this)
            adapter.fragmentList = fragmentList
            binding.trackPager.adapter = adapter
            val tabTitles = listOf("Music", "Playing")
            TabLayoutMediator(binding.tabLayout, binding.trackPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

        }
    //onCreate 끝
    }

fun startProcess() {}
class WorkerRunnable : Runnable {
    override fun run() {}
}