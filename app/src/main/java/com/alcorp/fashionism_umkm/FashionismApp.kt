package com.alcorp.fashionism_umkm

import android.app.Application
import com.alcorp.fashionism_umkm.di.networkModule
import com.alcorp.fashionism_umkm.di.repositoryModule
import com.alcorp.fashionism_umkm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FashionismApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FashionismApp)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }
}
