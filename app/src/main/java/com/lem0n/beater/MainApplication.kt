package com.lem0n.beater

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lem0n.beater.client.HomePageViewModel
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.UserRepositoryImpl
import com.lem0n.common.EventBus.EventBus
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.commonModule
import com.lem0n.hotspot.HotspotLib
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MainApplication : Application() {
    val appModule = module {
        single<UserRepository>(createdAtStart = true) { UserRepositoryImpl(this@MainApplication) }

        viewModel<HomePageViewModel> { HomePageViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            modules(appModule, commonModule)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
    }
}