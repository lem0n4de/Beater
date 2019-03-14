package com.lem0n.beater.junit5.first_start_test

import androidx.test.espresso.action.*
import com.agoda.kakao.screen.Screen
import com.lem0n.beater.ui.MainActivity
import com.lem0n.beater.R
import de.mannodermaus.junit5.ActivityTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ActivityTest(MainActivity::class)
class StartAppTest {
    @Test
    @DisplayName(
        "When the app is started for the first time, " +
                "show a dialog that makes user pick a role.")
    fun givenAppIsStarted_showRoleDialog() {
        Screen.onScreen<StartingScreen> {
            dialogTitle {
                matches { isDisplayed() }
            }

            dialogText {
                matches { isDisplayed() }
            }

            dialogButton1 {
                matches { withText(R.string.role_client) }
                matches { isClickable() }
                matches { isDisplayed() }
            }

            dialogButton2 {
                matches { withText(R.string.role_server) }
                matches { isClickable() }
                matches { isDisplayed() }
            }
        }
    }

    @Test
    @DisplayName(
        "When the app is started and a role dialog is shown," +
                "then the dialog can not be dismissed without selecting" +
                "one of the options."
    )
    fun givenStartDialogIsShown_UserHaveToSelectAnOption() {
        Screen.onScreen<StartingScreen> {
            dialogTitle {
                isDisplayed()
                perform {
                    GeneralClickAction(Tap.SINGLE, CoordinatesProvider {
                        var screenPos = IntArray(2)
                        it.getLocationOnScreen(screenPos)
                        val x = screenPos[0] + 300
                        val y = screenPos[1] - 300
                        floatArrayOf(x.toFloat(), y.toFloat())
                    }, Press.FINGER)
                }
                isDisplayed()
            }

            dialogButton1 {
                isClickable()
                click()
                doesNotExist()
            }

            dialogTitle {
                doesNotExist()
            }
        }
    }
}