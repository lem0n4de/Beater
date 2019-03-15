package com.lem0n.beater

import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.agoda.kakao.screen.Screen
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.database.Roles
import io.mockk.coVerify
import io.mockk.mockk
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class StartAppTest : KoinTest {
    val userRepo : UserRepository by inject()

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun before() {
        startKoin {
            modules(
                module {
                    single<UserRepository> { mockk<UserRepository>(relaxed = true) }
                }
            )
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    @Throws(Exception::class)
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
    @Throws(Exception::class)
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
    @Throws(Exception::class)
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
    @Throws(Exception::class)
    fun givenClientButtonPressed_saveValue() {
        Screen.onScreen<StartingScreen> {
            dialogButton1 {
                click()
            }

            coVerify(exactly = 1) {
                userRepo.insertUser(withArg {
                    it shouldNotEqual Roles.CLIENT
                })
            }
        }
    }
}
