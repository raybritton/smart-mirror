package app.raybritton.smartmirror.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.raybritton.smartmirror.data.models.Event

@Database(entities = [Event::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class MirrorDatabase : RoomDatabase() {
    abstract fun logDao(): StatusLogDao
}