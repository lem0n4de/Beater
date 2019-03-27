package com.lem0n.beater

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton

class MainActivityScreen : Screen<MainActivityScreen>() {
    val role_button_client = KButton { withId(R.id.role_button_client) }
    val role_button_server = KButton { withId(R.id.role_button_server) }
}