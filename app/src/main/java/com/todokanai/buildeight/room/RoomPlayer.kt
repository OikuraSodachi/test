package com.todokanai.buildeight.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_player")
class RoomPlayer {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var mTotal: Int? = null
    @ColumnInfo
    var mCurrent: Int? = null

    constructor(mTotal: Int?, mCurrent: Int?) {
        this.mTotal = mTotal
        this.mCurrent = mCurrent
    }

    override fun toString(): String {
        return "RoomPlayer(mTotal=$mTotal, mCurrent=$mCurrent)"
    }
}