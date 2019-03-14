package com.lem0n.beater

import android.app.Application
import com.lem0n.beater.data.UserRepository
import com.lem0n.beater.data.UserRepositoryImpl
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    val appModule = module {
        single<UserRepository> { UserRepositoryImpl(this@MainApplication) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}