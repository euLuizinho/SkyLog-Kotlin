package br.com.fiap.skylog

import android.app.Application
import br.com.fiap.skylog.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SkyLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SkyLogApplication)
            modules(appModule)
        }
    }
}
