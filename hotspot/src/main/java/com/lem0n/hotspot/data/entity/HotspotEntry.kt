package com.lem0n.hotspot.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

/**
 * Created by lem0n on 13/04/19.
 */
@Entity(tableName = "hotspot")
data class HotspotEntry(
    var remoteState : Boolean,
    var date: Instant
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
