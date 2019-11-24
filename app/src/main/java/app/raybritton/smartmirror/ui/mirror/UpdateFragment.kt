package app.raybritton.smartmirror.ui.mirror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.ui.arch.BaseFragment
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : BaseFragment<UpdateViewModel>(UpdateViewModel::class.java) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateAvailable.observe { updateAvailable ->
            update_text.visibility = if (updateAvailable) View.VISIBLE else View.GONE
        }
    }
}