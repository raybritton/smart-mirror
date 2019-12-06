package app.raybritton.smartmirror.ui.mirror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.raybritton.smartmirror.ui.arch.BaseFragment

class SceneryFragment : BaseFragment<SceneryViewModel>(SceneryViewModel::class.java) {
    private val sceneryView by lazy { SceneryView(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return sceneryView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dayOfChristmas.observe { sceneryView.dayOfChristmas = it }
        viewModel.mode.observe { sceneryView.mode = it }
    }
}