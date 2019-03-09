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
                inRoot { isPlatformPopup() }
                matches { withText(R.string.starting_dialog_title) }
                matches { isDisplayed() }
            }

            dialogText {
                inRoot { isPlatformPopup() }
                matches { withText(R.string.starting_dialog_text) }
                matches { isDisplayed() }
            }

            dialogButton1 {
                inRoot { isDialog() }
                matches { withText(R.string.role_server) }
                matches { isClickable() }
                matches { isDisplayed() }
            }

            dialogButton2 {
                inRoot { isDialog() }
                matches { withText(R.string.role_client) }
                matches { isClickable() }
                matches { isDisplayed() }
            }
        }
    }
}