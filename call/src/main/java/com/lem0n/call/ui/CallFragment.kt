package com.lem0n.call.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.lem0n.call.R
import kotlinx.android.synthetic.main.call_fragment.*
import org.koin.android.ext.android.inject

/**
 * Created by lem0n on 17/05/19.
 */
class CallFragment : Fragment() {
    companion object {
        fun newInstance() = CallFragment()
    }

    private val viewModel : CallViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.call_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        call_button.setOnClickListener {
            viewModel.callNumber(phone_number.text.toString())
        }

        viewModel.callState.observe(this, Observer {
            when (it) {
                CallState.CALL_STARTED -> {
                    showSnack("Call with ${phone_number.text.toString()} started.")
                }
                CallState.ON_HOLD -> {
                    showSnack("Call is on hold.")
                }
                CallState.CALL_ENDED -> {
                    showSnack("Call with ${phone_number.text.toString()} ended.")
                }
                CallState.ERROR -> {
                    showSnack("ERROR WHILE CALLING.")
                }
                else -> {
                    showSnack("ERROR WHILE CALLING.")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val ac = activity as AppCompatActivity
        ac.supportActionBar!!.title = getString(R.string.call_fragment_title)
    }

    private fun showSnack(text : String) {
        Snackbar.make(View(context), text, Snackbar.LENGTH_LONG).show()
    }
}