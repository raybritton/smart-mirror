package app.raybritton.smartmirror.ui.mirror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.ext.createPulseAnimation
import app.raybritton.smartmirror.ui.arch.BaseFragment
import kotlinx.android.synthetic.main.fragment_alert.*

class AlertView : BaseFragment<AlertViewModel>(AlertViewModel::class.java) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = alert_actual.createPulseAnimation()

        viewModel.hasAlert.observe { hasAlert ->
            if (hasAlert) {
                alert_actual.visibility = View.VISIBLE
                animation.start()
            } else {
                alert_actual.visibility = View.GONE
                animation.cancel()
            }
        }
    }
}