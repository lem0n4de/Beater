package com.lem0n.beater

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
            .setTitle(R.string.starting_dialog_title)
            .setMessage(R.string.starting_dialog_text)
            .setPositiveButton(R.string.role_client, null)
            .setNegativeButton(R.string.role_server, null)
            .show()
        Log.d(logTag, "Showed dialog. Ending onCreate.")
    }
}
