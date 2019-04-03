package com.lem0n.beater

import android.app.Application
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.UserRepositoryImpl
import com.lem0n.common.EventBus.EventBus
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.Receivers.ServerReceiver
import com.lem0n.common.commonModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MainApplication : Application() {
    val appModule = module {
        single<UserRepository> { UserRepositoryImpl(this@MainApplication) }
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
    }
}