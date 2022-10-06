package com.todokanai.buildeight.adapter

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.buildeight.TrackTool
import com.todokanai.buildeight.activity.MainActivity
import com.todokanai.buildeight.databinding.ItemRecyclerBinding
import com.todokanai.buildeight.room.RoomHelper
import com.todokanai.buildeight.room.RoomTrack
import com.todokanai.buildeight.service.ForegroundPlayService.Companion.mediaPlayer
import java.text.SimpleDateFormat

class TrackRecyclerAdapter : RecyclerView.Adapter<TrackRecyclerAdapter.Holder>() {
    var trackList = mutableListOf<RoomTrack>()
    //----변수 선언--------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val roomTrack = trackList[position]
        holder.setTrack(roomTrack,position)
    }

    inner class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {//원래는 홀더 밖에 있어야할 클래스 구겨넣음
        var trackUri: Uri? = null
        var current: Int? = null         // 해당 홀더에서 다루는 RoomTrack 정보
        //-----------------------
        init {
            binding.root.setOnClickListener {
                    if(mediaPlayer != null) {       //이미 재생되고있는게 있으면
                        mediaPlayer?.release()      //재생 중단후
                        mediaPlayer = null          //재생중이 아닌상태로 초기화, //재생중인 동일곡 클릭시 중단안하고 지속하는 코드 만들기 //
                    }
                mediaPlayer = MediaPlayer.create(binding.root.context, trackUri)    //mediaPlayer에 음원id(trackUri) 넣기
                TrackTool(null,current).start()                // 재생 개시
            }     //---------아이템뷰가 클릭되면 음원 재생
        }

        fun setTrack(roomTrack: RoomTrack, position:Int) {
            binding.run {
                imageAlbum.setImageURI(roomTrack.getAlbumUri())     //앨범이미지 투영
                textArtist.text = roomTrack.artist
                textTitle.text = "$position  ${roomTrack.title}"
                val duration = SimpleDateFormat("mm:ss").format(roomTrack.duration)
                textDuration.text = duration
            }                                   // 홀더에 내용 추가
            this.trackUri = roomTrack.getTrackUri()
            this.current = position
        }
    }
}