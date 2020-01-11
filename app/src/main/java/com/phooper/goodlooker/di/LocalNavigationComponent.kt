package com.phooper.goodlooker.di

import com.phooper.goodlooker.presentation.drawer.DrawerPresenter
import com.phooper.goodlooker.ui.drawer.DrawerFlowFragment
import com.phooper.goodlooker.ui.drawer.DrawerFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, LocalNavigationModule::class])
@Singleton
interface LocalNavigationComponent {

    fun inject(drawerFlowFragment: DrawerFlowFragment)
    fun inject(drawerFlowFragment: DrawerPresenter)

}