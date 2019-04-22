package com.lem0n.hotspot.data.database.dao

import androidx.room.*
import com.lem0n.hotspot.data.database.entity.HotspotEntry
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by lem0n on 14/04/19.
 */

@Dao
interface HotspotDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(hotspotEntry : HotspotEntry) : Completable

    @Query("SELECT * FROM hotspot WHERE id == :id")
    fun getEntry(id : Int) : Single<HotspotEntry>

    @Query("SELECT * FROM hotspot")
    fun getAllEntries() : Observable<List<HotspotEntry>>

    @Query("SELECT * FROM hotspot ORDER BY id DESC LIMIT 1")
    fun getLastEntry() : Single<HotspotEntry>

    @Delete
    fun delete(hotspotEntry : HotspotEntry)
}