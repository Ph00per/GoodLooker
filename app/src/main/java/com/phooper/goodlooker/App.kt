package com.phooper.goodlooker

import android.app.Application
import com.phooper.goodlooker.di.AppComponent
import com.phooper.goodlooker.di.AppModule
import com.phooper.goodlooker.di.DaggerAppComponent


class App : Application() {

    companion object {
        lateinit var daggerComponent: AppComponent

    }

    override fun onCreate() {
        super.onCreate()

        daggerComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}
