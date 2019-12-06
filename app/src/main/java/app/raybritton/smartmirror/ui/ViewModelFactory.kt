package app.raybritton.smartmirror.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.raybritton.smartmirror.ui.mirror.AlertViewModel
import app.raybritton.smartmirror.ui.mirror.MirrorViewModel
import app.raybritton.smartmirror.ui.mirror.SceneryViewModel
import app.raybritton.smartmirror.ui.mirror.UpdateViewModel
import app.raybritton.smartmirror.ui.mirror.weather.current.CurrentWeatherViewModel
import app.raybritton.smartmirror.ui.mirror.weather.today.TodayWeatherViewModel
import app.raybritton.smartmirror.ui.mirror.weather.tomorrow.TomorrowWeatherViewModel
import app.raybritton.smartmirror.ui.settings.LogStatusViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java) -> CurrentWeatherViewModel() as T
            modelClass.isAssignableFrom(TodayWeatherViewModel::class.java) -> TodayWeatherViewModel() as T
            modelClass.isAssignableFrom(AlertViewModel::class.java) -> AlertViewModel() as T
            modelClass.isAssignableFrom(TomorrowWeatherViewModel::class.java) -> TomorrowWeatherViewModel() as T
            modelClass.isAssignableFrom(MirrorViewModel::class.java) -> MirrorViewModel() as T
            modelClass.isAssignableFrom(LogStatusViewModel::class.java) -> LogStatusViewModel() as T
            modelClass.isAssignableFrom(UpdateViewModel::class.java) -> UpdateViewModel() as T
            modelClass.isAssignableFrom(SceneryViewModel::class.java) -> SceneryViewModel() as T
            else -> throw IllegalArgumentException("Can't create view model for ${modelClass.canonicalName}")
        }
    }

}