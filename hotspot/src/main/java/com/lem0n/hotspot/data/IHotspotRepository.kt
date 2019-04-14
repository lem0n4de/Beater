package com.lem0n.hotspot.data

import com.lem0n.hotspot.data.database.entity.HotspotEntry
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by lem0n on 14/04/19.
 */
interface IHotspotRepository {
    suspend fun getLastEntry() : Single<HotspotEntry>
    suspend fun getEntryWith(id : Int?) : Single<HotspotEntry>
    suspend fun getAllEntries() : Observable<List<HotspotEntry>>
    suspend fun deleteWithId(id : Int?)
}