package com.lem0n.beater.junit5.first_start_test

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.lem0n.beater.R

class StartingScreen : Screen<StartingScreen>() {
    val dialogTitle = KView { withText(R.string.starting_dialog_title) }
    val dialogText = KView { withText(R.string.starting_dialog_text) }
    val dialogButton1 = KButton { withId(android.R.id.button1) }
    val dialogButton2 = KButton { withId(android.R.id.button2) }
}