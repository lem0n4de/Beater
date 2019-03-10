package com.lem0n.beater

import com.agoda.kakao.screen.Screen
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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
                matches { withText(R.string.role_server) }
                matches { isClickable() }
                matches { isDisplayed() }
            }

            dialogButton2 {
                matches { withText(R.string.role_client) }
                matches { isClickable() }
                matches { isDisplayed() }
            }
        }
    }
}