package app.raybritton.smartmirror.ui.arch

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.raybritton.smartmirror.ui.ViewModelFactory

abstract class BaseFragment<T : BaseViewModel>(viewModelClass: Class<T>) : Fragment() {
    protected val viewModel : T by lazy { ViewModelFactory().create(viewModelClass) }

    protected fun <T> LiveData<T>.observe(callback: (T) -> Unit) {
        this.observe(this@BaseFragment, Observer { callback(it) })
    }

    protected fun hide(vararg view: View) {
        view.forEach { it.visibility = View.GONE }
    }

    protected fun show(vararg view: View) {
        view.forEach { it.visibility = View.VISIBLE }
    }
}
