package app.raybritton.smartmirror.data.models

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.database.EVENT_TABLE_NAME
import org.joda.time.DateTime

@Entity(tableName = EVENT_TABLE_NAME)
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val time: DateTime,
    val type: Type,
    val message: String
) {
    enum class Type(@StringRes val display: Int, @ColorRes val color: Int) {
        APP(R.string.type_app, R.color.type_app),
        NET(R.string.type_net, R.color.type_net),
        POWER(R.string.type_power, R.color.type_power)
    }

    companion object {
        fun create(type: Type, message: String): Event {
            return Event(
                null,
                DateTime.now(),
                type,
                message
            )
        }
    }
}

