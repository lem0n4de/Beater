package com.lem0n.beater

import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import androidx.test.rule.ServiceTestRule
import com.lem0n.beater.server.ServerService
import com.lem0n.beater.server.getServerService
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.*
import org.junit.*

@MediumTest
class ServerServiceTest {
    @get:Rule
    val serviceTestRule = ServiceTestRule()

    private var service : ServerService? = null
    private var serviceMessenger : Messenger? = null

    @Before
    fun initializeService() {
        val binder : IBinder = serviceTestRule.bindService(
            Intent(ApplicationProvider.getApplicationContext(), ServerService::class.java)
        )

        service = binder.getServerService()
        serviceMessenger = Messenger(binder)
    }

    @After
    fun unbindFromService() {
        service = null
        serviceMessenger = null
        serviceTestRule.unbindService()
    }

    @Test
    fun testSuccessfulStart() {
        service shouldBeInstanceOf ServerService::class
        serviceMessenger shouldNotBe null
    }

    @Test
    fun registersAndUnregistersClientsSuccessfully() {
        // clientList should be empty at first
        val myMessenger = serviceMessenger!!
        service!!.clientList shouldNotContain myMessenger
        service!!.registerClient(myMessenger)
        service!!.clientList shouldContain myMessenger

        service!!.unregisterClient(myMessenger)
        service!!.clientList shouldNotContain myMessenger
    }
}