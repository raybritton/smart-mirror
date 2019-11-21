package app.raybritton.smartmirror.ui.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.raybritton.smartmirror.R
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.element_app.view.*

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val appInfos = packageManager.getInstalledApplications(0)
            .map {
                it.icon
                val name = it.name
                val packageName = it.packageName
                AppInfo(name, packageName)
            }

        app_list.layoutManager = LinearLayoutManager(this)
        app_list.adapter = Adapter(packageManager, layoutInflater, appInfos)

        app_progress.visibility = View.GONE
    }

    private data class Adapter(
        private val packageManager: PackageManager,
        private val layoutInflater: LayoutInflater,
        private val data: List<AppInfo>
    ) :
        RecyclerView.Adapter<Adapter.AppViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            val view = layoutInflater.inflate(R.layout.element_app, parent, false)
            return AppViewHolder(view)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
            holder.itemView.app_icon.setImageDrawable(packageManager.getApplicationIcon(data[position].packageName))
            holder.itemView.app_name.text = data[position].name
            holder.itemView.app_package.text = data[position].packageName
        }

        class AppViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

    private data class AppInfo(
        val name: String,
        val packageName: String
    )

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, AppActivity::class.java))
        }
    }
}