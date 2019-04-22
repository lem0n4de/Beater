package com.lem0n.hotspot

import java.util.*

/**
 * Created by lem0n on 16/04/19.
 */
object SignalContract {
    val ENABLE_HOTSPOT = UUID.fromString("ec5e24ff-d0cb-47e6-83d4-d04b195a3c0c")
    val DISABLE_HOTSPOT = UUID.fromString("542784ae-9881-45eb-ab3f-31f2e24b3ab8")

    val ENABLE_HOTSPOT_FAILED = UUID.fromString("9b9618ce-7c85-43ab-a724-eba796d15108")
    val ENABLE_HOTSPOT_SUCCESSFUL = UUID.fromString("b01182c0-1d0f-48d7-988d-b23946e45b3f")

    val DISABLE_HOTSPOT_FAILED = UUID.fromString("5aff01dd-9cac-4b3f-9e2f-74c0f0a03ab4")
    val DISABLE_HOTSPOT_SUCCESSFUL = UUID.fromString("60b86eef-4f96-4846-9f37-28cb3a78752c")
}