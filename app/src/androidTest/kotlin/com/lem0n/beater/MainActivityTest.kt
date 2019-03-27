package com.lem0n.beater

import android.bluetooth.BluetoothAdapter
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnitRunner
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.agoda.kakao.screen.Screen
import com.lem0n.beater.client.ClientActivity
import com.lem0n.beater.server.ServerActivity
import org.amshove.kluent.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class MainActivityTest {
    @get:Rule
    val mainActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    val clientActivityIntentRule = IntentsTestRule(ClientActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val res = InstrumentationRegistry.getInstrumentation().context.resources

    @Test
    fun givenBluetoothIsNotEnabled_ShowBluetoothDialogWhenServerButtonIsPressed() {
        changeBluetoothState(false)
        Screen.onScreen<MainActivityScreen> {
            role_button_server {
                isDisplayed()
                hasText(R.string.role_server)
                click()
            }

            val bluetoothText = device.findObject(
                UiSelector()
                    .clickable(false)
                    .textContains("Beater")
                    .textContains("Bluetooth")
            )

            val enableBluetooth = device.findObject(
                UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1)
            )
            val disableBluetooth = device.findObject(
                UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(0)
            )
            bluetoothText.waitForExists(5000L)

            enableBluetooth.exists() shouldBe true
            disableBluetooth.exists() shouldBe true

            disableBluetooth.click()
        }
    }

    @Test
    fun givenBluetoothIsNotEnabled_ShowBluetoothDialogWhenClientButtonIsPressed() {
        changeBluetoothState(false)
        Screen.onScreen<MainActivityScreen> {
            role_button_client {
                isDisplayed()
                hasText(R.string.role_client)
                click()
            }

            val bluetoothText = device.findObject(
                UiSelector()
                    .clickable(false)
                    .textContains("Beater")
                    .textContains("Bluetooth")
            )

            val enableBluetooth = device.findObject(
                UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1)
            )
            val disableBluetooth = device.findObject(
                UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(0)
            )
            bluetoothText.waitForExists(5000L)

            enableBluetooth.exists() shouldBe true
            disableBluetooth.exists() shouldBe true

            disableBluetooth.click()
        }
    }

    @Test
    fun givenThatBluetoothIsEnabled_WhenClientButtonIsPressed_ShowClientActivity() {
        changeBluetoothState(true)

        Screen.onScreen<MainActivityScreen> {
            role_button_client.click()
        }

        Intents.intended(IntentMatchers.hasComponent(ClientActivity::class.java.name))
    }

    @Test
    fun givenThatBluetoothIsEnabled_WhenServerButtonIsPressed_ShowServerActivity() {
        changeBluetoothState(true)

        Screen.onScreen<MainActivityScreen> {
            role_button_server.click()
        }

        Intents.intended(IntentMatchers.hasComponent(ServerActivity::class.java.name))
    }

    private fun changeBluetoothState(state : Boolean) {
        val bt = BluetoothAdapter.getDefaultAdapter()
        if (state == true) {
            bt.enable()
            while (!bt.isEnabled) {
                Thread.sleep(250L)
            }
        } else {
            bt.disable()
            while (bt.isEnabled) {
                Thread.sleep(250L)
            }
        }
    }
}