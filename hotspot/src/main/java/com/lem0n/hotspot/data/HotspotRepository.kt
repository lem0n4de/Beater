package com.lem0n.hotspot.data

import android.content.Context
import androidx.room.EmptyResultSetException
import com.lem0n.hotspot.data.database.HotspotDatabase
import com.lem0n.hotspot.data.database.entity.HotspotEntry
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.Instant
import timber.log.Timber

class RepositoryException(val msg : String? = null) : Exception(msg)

class HotspotRepository(private val context : Context) : IHotspotRepository {
    private val database = HotspotDatabase.getDatabase(context)
    private val hotspotDao = database.hotspotDao()

    @Throws
    override suspend fun getLastEntry(): Single<HotspotEntry> {
        val last = hotspotDao.getLastEntry()
        Timber.d("Got last entry with id ${last}")
        return last
    }

    override suspend fun getEntryWith(id: Int?): Single<HotspotEntry> {
        id?.let {
            val entry = hotspotDao.getEntry(it)
            Timber.d("Got hotspot entry with id $it")
            return entry
        } ?: throw RepositoryException("ID can not be null.")
    }

    override suspend fun getAllEntries(): Observable<List<HotspotEntry>> {
        val allEntries = hotspotDao.getAllEntries()
        Timber.d("Got all entries.")
        return allEntries
    }

    override suspend fun deleteWithId(id: Int?) {
        id?.let {
            try {
                val toBeDeletedEntry = hotspotDao.getEntry(id).blockingGet()
                hotspotDao.delete(toBeDeletedEntry)
                Timber.d("Deleted entry with id $it")
            } catch (e : Exception) {
                Timber.e(e)
                throw RepositoryException(e.message)
            }
        } ?: throw RepositoryException("ID can not be null")
    }

    override suspend fun newEntry(ssid: String?, password: String?, state: Boolean): Completable {
        if (ssid == null) throw RepositoryException("SSID can not be null.")
        val pass = if (password != null) password else ""
        val newEntry = HotspotEntry(state, ssid, pass, Instant.now())
        Timber.i("Adding new entry.")
        return hotspotDao.insert(newEntry)
    }
}