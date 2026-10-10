package com.fashionism.fashionismuserapp

import android.app.Application
import com.fashionism.fashionismuserapp.di.dataModule
import com.fashionism.fashionismuserapp.di.networkModule
import com.fashionism.fashionismuserapp.di.viewModelModule
import com.fashionism.fashionismuserapp.tools.EdgeToEdgeCallbacks
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FashionismApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FashionismApp)
            modules(networkModule, dataModule, viewModelModule)
        }
        registerActivityLifecycleCallbacks(EdgeToEdgeCallbacks)
    }
}
