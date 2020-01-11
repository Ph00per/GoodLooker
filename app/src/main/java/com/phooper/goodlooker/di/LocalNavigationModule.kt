package com.phooper.goodlooker.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class LocalNavigationModule {
    private val localCicerone = Cicerone.create()

    @Provides
    @Singleton
    fun provideLocalRouter(): Router = localCicerone.router

    @Provides
    @Singleton
    fun provideLocalNavigatorHolder(): NavigatorHolder = localCicerone.navigatorHolder
}