package com.lem0n.hotspot.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
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

    private val SSID_KEY : String = "ssid_key"
    private var ssid : String = ""
    private val PASSWORD_KEY : String = "password_key"
    private var password : String = ""
    private val STATE_KEY : String = "state_key"
    private var _state : Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        turn_hotspot_on.setOnClickListener {
            Timber.d("Pressed turn hotspot on button.")
            ssid = ssid_text_field.text.toString()
            password = password_text_field.text.toString()
            viewModel.turnHotspotOn(ssid, password)
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
                _state = true
            } else {
                hotspot_state.text = "Kapalı"
                turn_hotspot_on.visibility = View.VISIBLE
                turn_hotspot_off.visibility = View.GONE
                _state = false
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

    override fun onStop() {
        super.onStop()
        val hotspotSettings = context?.getSharedPreferences("HOTSPOT_PREFS", 0)
        hotspotSettings?.edit {
            putString(SSID_KEY, ssid)
            putString(PASSWORD_KEY, password)
            putBoolean(STATE_KEY, _state)
        }
        Timber.i("App state saved.")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("Recovering state.")
        val hotspotSettings = context?.getSharedPreferences("HOTSPOT_PREFS", 0)

        val saved_ssid = hotspotSettings?.getString(SSID_KEY, "")!!
        val saved_pass = hotspotSettings.getString(PASSWORD_KEY, "")
        val saved_state = hotspotSettings.getBoolean(STATE_KEY, false)

        ssid_text_field.text = saved_ssid
        password_text_field.text = saved_pass
        if (saved_state == true) {
            hotspot_state.text = "Açık"
            turn_hotspot_on.visibility = View.GONE
            turn_hotspot_off.visibility = View.VISIBLE
            _state = true
        } else {
            hotspot_state.text = "Kapalı"
            turn_hotspot_on.visibility = View.VISIBLE
            turn_hotspot_off.visibility = View.GONE
            _state = false
        }
    }
}
