package com.todokanai.buildeight.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RoomPlayerDao {
    @Query("select * from room_player")
    fun getAll() : List<RoomPlayer>

    @Insert(onConflict = REPLACE)
    fun insert(roomPlayer: RoomPlayer)

    @Delete
    fun delete(roomPlayer: RoomPlayer)

    @Query("Delete from room_player")
    fun deleteAll()
}