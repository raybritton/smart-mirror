package app.raybritton.smartmirror.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.raybritton.smartmirror.data.models.Event
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

const val EVENT_TABLE_NAME = "events"
private const val COL_ID = "id"
private const val COL_TIME = "time"
private const val COL_TYPE = "type"
private const val COL_MESSAGE = "message"
private const val COL_IMPORTANT = "important"

@Dao
interface StatusLogDao {
    @Query("SELECT * FROM $EVENT_TABLE_NAME WHERE DATETIME(:time, 'start of day') = DATETIME($COL_TIME, 'start of day')")
    fun getEventsForDay(time: String): Single<List<Event>>

    @Query("SELECT * FROM $EVENT_TABLE_NAME WHERE DATETIME(:time, 'start of day') = DATETIME($COL_TIME, 'start of day') AND $COL_IMPORTANT = 1")
    fun getImportantEventsForDay(time: String): Flowable<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(event: Event): Completable
}