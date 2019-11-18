package app.raybritton.smartmirror.data.database

import androidx.room.TypeConverter
import app.raybritton.smartmirror.data.models.Event
import org.joda.time.DateTime

class Converters {

    @TypeConverter
    fun fromEventType(event: Event.Type) = event.name

    @TypeConverter
    fun toEventType(value: String) = Event.Type.valueOf(value)

    @TypeConverter
    fun fromDateTime(dateTime: DateTime) = dateTime.toString()

    @TypeConverter
    fun toDateTime(value: String) = DateTime.parse(value)
}