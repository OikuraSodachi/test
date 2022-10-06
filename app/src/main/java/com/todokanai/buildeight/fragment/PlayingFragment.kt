package com.todokanai.buildeight.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.todokanai.buildeight.TrackTool
import com.todokanai.buildeight.databinding.FragmentPlayingBinding
import com.todokanai.buildeight.service.ForegroundPlayService

class PlayingFragment : Fragment() {

    val binding by lazy { FragmentPlayingBinding.inflate(layoutInflater) }
    var mediaPlayer = ForegroundPlayService.mediaPlayer

    val mRepeatSet : Int
        get(){return TrackTool(null,null).iconset2()}

    val mIconSet : Int
        get(){return TrackTool(null,null).iconset()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    //sets the playing file progress to the same seekbar progressive, in relative scale
                    mediaPlayer?.seekTo(progress)

                    //Also updates the textView because the coroutine only runs every 1 second
                    binding.songCurrentProgress.text = "${progress}"
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        binding.seekBar.setOnSeekBarChangeListener(seekBarListener)

        val a = mediaPlayer?.trackInfo
        Log.d("tested","$a")
        //binding.playerImage.setImageURI(Player().playing!!.getAlbumUri())  // 앨범 이미지

        binding.repeatButton.setOnClickListener{TrackTool(activity,null).replay();binding.repeatButton.setImageResource(mRepeatSet)}
        binding.previousButton.setOnClickListener{ TrackTool(activity,null).prev()}
        binding.playPauseButton.setOnClickListener{ TrackTool(activity,null).pauseplay();binding.playPauseButton.setImageResource(mIconSet); Log.d("tested","$mIconSet")}
        binding.nextButton.setOnClickListener{TrackTool(activity,null).next()}

        return binding.root
    }
}