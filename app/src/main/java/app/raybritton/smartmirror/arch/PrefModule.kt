package app.raybritton.smartmirror.arch

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences

object PrefModule {
    private val sharedPrefs by lazy {
        MirrorModule.application.getSharedPreferences("mirror.prefs", Context.MODE_PRIVATE)
    }

    private val rxPrefs by lazy {
        RxSharedPreferences.create(sharedPrefs)
    }

    const val NO_EVENT = -1L

    val latestUnreadImportantEventId by lazy {
        rxPrefs.getLong("unread_important_event_id.long", NO_EVENT)
    }
}