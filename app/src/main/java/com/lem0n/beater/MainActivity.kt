package com.lem0n.beater

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lem0n.beater.client.ClientActivity
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.database.Roles
import com.lem0n.beater.server.ServerActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val userRepo : UserRepository by inject()

    private val REQUEST_ENABLE_BT_CLIENT = 1
    private val REQUEST_ENABLE_BT_SERVER = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        role_button_client.setOnClickListener { _ ->
            Timber.d("onClickListener for Client")
            val btState = BluetoothAdapter.getDefaultAdapter().isEnabled
            GlobalScope.launch(Dispatchers.IO) {
                userRepo.insertUser(Roles.CLIENT)
            }
            if (btState == true) {
                Intent(this, ClientActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also {
                    startActivityForResult(it, REQUEST_ENABLE_BT_CLIENT)
                }
            }
        }

        role_button_server.setOnClickListener { _ ->
            Timber.d("onClickListener for Server")
            val btState = BluetoothAdapter.getDefaultAdapter().isEnabled
            GlobalScope.launch(Dispatchers.IO) {
                userRepo.insertUser(Roles.SERVER)
            }
            if (btState == true) {
                Intent(this, ServerActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also {
                    startActivityForResult(it, REQUEST_ENABLE_BT_SERVER)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT_CLIENT) {
            if (resultCode == Activity.RESULT_OK) {
                Intent(this, ClientActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                // TODO
            }
        } else if (requestCode == REQUEST_ENABLE_BT_SERVER) {
            if (resultCode == Activity.RESULT_OK) {
                Intent(this, ServerActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
}
