package app.raybritton.smartmirror.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.raybritton.smartmirror.data.models.Event

const val EVENT_TABLE_NAME = "events"
private const val COL_ID = "id"
private const val COL_TIME = "time"
private const val COL_TYPE = "type"
private const val COL_MESSAGE = "message"

@Dao
interface StatusLogDao {
    @Query("SELECT * FROM $EVENT_TABLE_NAME WHERE DATETIME(:time, 'start of day') = DATETIME($COL_TIME, 'start of day')")
    suspend fun getEventsForDay(time: String): List<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(event: Event)
}