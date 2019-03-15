package com.lem0n.beater.junit5.first_start_test

import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import com.agoda.kakao.screen.Screen
import com.lem0n.beater.R
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.database.Roles
import com.lem0n.beater.MainActivity
import de.mannodermaus.junit5.ActivityTest
import io.mockk.coVerify
import io.mockk.mockk
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@ActivityTest(MainActivity::class)
class StartAppTest : KoinTest {

    val userRepo : UserRepository by inject()

    @BeforeEach
    fun before() {
        startKoin {
            modules(
                module {
                    single<UserRepository> { mockk<UserRepository>(relaxed = true) }
                }
            )
        }
    }

    @AfterEach
    fun after() {
        stopKoin()
    }

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

    @Test
    @DisplayName("Given server button is pressed, " +
            "save the value to database.")
    fun givenServerButtonPressed_saveValue() {
        Screen.onScreen<StartingScreen> {
            dialogButton2 {
                click()
            }

            coVerify(exactly = 1) {
                userRepo.insertUser(withArg {
                    it shouldEqual Roles.SERVER
                })
            }
        }
    }

    @Test
    @DisplayName("Given client button is pressed, " +
            "save the value to database.")
    fun givenClientButtonPressed_saveValue() {
        Screen.onScreen<StartingScreen> {
            dialogButton1 {
                click()
            }

            coVerify(exactly = 1) {
                userRepo.insertUser(withArg {
                    it shouldEqual Roles.CLIENT
                })
            }
        }
    }
}