package com.lem0n.hotspot

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val hotspotModule = module {
    viewModel<HotspotViewModel> { HotspotViewModel() }
}