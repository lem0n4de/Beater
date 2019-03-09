package com.lem0n.beater

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton

class StartingScreen : Screen<StartingScreen>() {
    val dialogTitle = KView { withId(androidx.appcompat.R.id.alertTitle) }
    val dialogText = KView { withId(androidx.appcompat.R.id.text) }
    val dialogButton1 = KButton { withId(android.R.id.button1) }
    val dialogButton2 = KButton { withId(android.R.id.button2) }
}