    package com.todokanai.buildeight.fragment

    import android.content.Context
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.room.Room
    import com.todokanai.buildeight.adapter.TrackRecyclerAdapter
    import com.todokanai.buildeight.databinding.FragmentTrackBinding
    import com.todokanai.buildeight.room.RoomHelper

    class TrackFragment : Fragment() {
        val binding by lazy { FragmentTrackBinding.inflate(layoutInflater) }

        lateinit var helper : RoomHelper
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            val adapter = TrackRecyclerAdapter()
            val ct : Context = requireContext()
            helper = Room.databaseBuilder(ct,RoomHelper::class.java,"room_db")
                .allowMainThreadQueries()
                .build()

            adapter.trackList.addAll(helper.roomTrackDao().getAll())
            Log.d("tested","loaded")
            binding.trackRecyclerView.adapter = adapter
            binding.trackRecyclerView.layoutManager = LinearLayoutManager(context)

            binding.swipe.setOnRefreshListener {
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false          //------swipe 해서 목록 새로고침
            }
            return binding.root
        }
    }