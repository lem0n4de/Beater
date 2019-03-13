package com.lem0n.beater

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(logTag, "Building/showing material alert dialog.")
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.starting_dialog_title)
            .setMessage(R.string.starting_dialog_text)
            .setPositiveButton(R.string.role_client, { di, int ->
                if (int == DialogInterface.BUTTON_POSITIVE) {
                    Log.i(logTag, "Client selected. Starting client activity.")
                    // TODO Start client activity
                }
                di.dismiss()
            })
            .setNegativeButton(R.string.role_server, { di, int ->
                if (int == DialogInterface.BUTTON_NEGATIVE) {
                    Log.i(logTag, "Server selected. Starting server activity.")
                    // TODO Start server activity
                }
                di.dismiss()
            })
            .show()
        Log.d(logTag, "Showed dialog. Ending onCreate.")
    }
}
