package com.lem0n.hotspot.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lem0n.hotspot.R
import kotlinx.android.synthetic.main.change_ssid_and_password_dialog.view.*
import kotlinx.android.synthetic.main.hotspot_fragment.*
import org.koin.android.ext.android.inject
import timber.log.Timber


class HotspotFragment : Fragment() {

    companion object {
        fun newInstance() = HotspotFragment()
    }

    private val viewModel: HotspotViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hotspot_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        turn_hotspot_on.setOnClickListener {
            Timber.d("Pressed turn hotspot on button.")
            val ssid = ssid_text_field.text.toString()
            val pass = password_text_field.text.toString()
            viewModel.turnHotspotOn(ssid, pass)
        }
        turn_hotspot_off.setOnClickListener {
            Timber.d("Pressed turn hotspot off button.")
            viewModel.turnHotspotOff()
        }
        change_ssid_and_password.setOnClickListener {
            Timber.d("Showing dialog.")
            showChangeDialog()
        }

        viewModel.state.observe(this, Observer {
            if (it.remoteState == true) {
                hotspot_state.text = "Açık"
                turn_hotspot_on.visibility = View.GONE
                turn_hotspot_off.visibility = View.VISIBLE
            } else {
                hotspot_state.text = "Kapalı"
                turn_hotspot_on.visibility = View.VISIBLE
                turn_hotspot_off.visibility = View.GONE
            }
        })

        viewModel.lastOperationDone.observe(this, Observer {
            loadingCircle.visibility = if (it == true) View.GONE else View.VISIBLE
        })
    }

    override fun onResume() {
        super.onResume()
        val ac = activity as AppCompatActivity
        ac.supportActionBar!!.title = getString(R.string.hotspot_page_title)
    }

    fun showChangeDialog() {
        val dialog = MaterialAlertDialogBuilder(context)
        val layout = LayoutInflater.from(context).inflate(R.layout.change_ssid_and_password_dialog, null)
        dialog.setView(layout)
        dialog.setPositiveButton(R.string.change_dialog_positive_button, { di, _ ->
            val newSsid = layout.change_dialog_ssid.text.toString()
            val newPassword = layout.change_dialog_password.text.toString()
            Timber.d("SSID is $newSsid")
            ssid_text_field.text = newSsid
            password_text_field.text = newPassword
            di.dismiss()
        })
        dialog.setNegativeButton(R.string.change_dialog_negative_button, { di, _ ->
            di.dismiss()
        })
        dialog.show()
    }
}
