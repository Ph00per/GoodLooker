package com.phooper.goodlooker

import android.app.Application
import com.phooper.goodlooker.di.*


class App : Application() {

    companion object {
        lateinit var daggerComponent: AppComponent
        lateinit var localNavigationComponent: LocalNavigationComponent

    }

    override fun onCreate() {
        super.onCreate()

        daggerComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()

        localNavigationComponent = DaggerLocalNavigationComponent.builder().localNavigationModule(LocalNavigationModule()).build()

}}
