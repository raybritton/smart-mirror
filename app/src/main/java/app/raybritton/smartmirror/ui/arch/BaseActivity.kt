package app.raybritton.smartmirror.ui.arch

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.raybritton.smartmirror.ui.ViewModelFactory

abstract class BaseActivity<T : BaseViewModel>(viewModelClass: Class<T>) : AppCompatActivity() {
    protected val viewModel : T by lazy { ViewModelFactory().create(viewModelClass) }

    protected fun <T> LiveData<T>.observe(callback: (T) -> Unit) {
        this.observe(this@BaseActivity, Observer { callback(it) })
    }
}
