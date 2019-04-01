package com.lem0n.beater

import android.app.Application
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.UserRepositoryImpl
import com.lem0n.base.EventBus.EventBus
import com.lem0n.base.EventBus.IEventBus
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MainApplication : Application() {
    val appModule = module {
        single<UserRepository> { UserRepositoryImpl(this@MainApplication) }
        single<IEventBus> { EventBus }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            modules(appModule)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}