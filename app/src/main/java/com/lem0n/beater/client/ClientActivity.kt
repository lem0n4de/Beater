package com.lem0n.beater.client

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.lem0n.beater.R
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectionFailed
import kotlinx.android.synthetic.main.activity_client.*
import org.koin.android.ext.android.inject
import timber.log.Timber

@SuppressLint("CheckResult")
class ClientActivity : AppCompatActivity() {
    private val bus : IEventBus by inject()
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        navController = Navigation.findNavController(this, R.id.fragment_container)
        bottom_navigation.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
        Timber.d("Navigation set up.")


        Intent(this, ClientService::class.java).also {
            startService(it)
        }
        Timber.d("Started activity.")
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}
