package com.phooper.goodlooker.ui.drawer

import android.os.Bundle
import androidx.core.view.GravityCompat
import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.ui.favourite.FavouriteListFragment
import com.phooper.goodlooker.ui.feedlist.*
import com.phooper.goodlooker.ui.global.BaseFragment
import kotlinx.android.synthetic.main.drawer_flow_fragment.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject

class DrawerFlowFragment : BaseFragment() {

    @Inject
    lateinit var localNavigatorHolder: NavigatorHolder

    @Inject
    lateinit var localRouter: Router

    override val layoutRes = R.layout.drawer_flow_fragment

    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.content_container) as? BaseFragment

    private val drawerFragment
        get() = childFragmentManager.findFragmentById(R.id.nav_drawer_container) as? DrawerFragment

    private val localNavigator: Navigator by lazy {
        object : SupportAppNavigator(this.activity, childFragmentManager, R.id.content_container) {

            override fun applyCommands(commands: Array<out Command>?) {
                super.applyCommands(commands)
                updateNavDrawer()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.localNavigationComponent.inject(this)

        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.nav_drawer_container, DrawerFragment())
                .commitNow()

            localRouter.newRootScreen(Screens.WholeSite)

        } else {
            updateNavDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        localNavigatorHolder.setNavigator(localNavigator)
    }

    override fun onPause() {
        localNavigatorHolder.removeNavigator()
        super.onPause()
    }

    fun openNavDrawer(open: Boolean) {
        if (open) drawer_layout?.openDrawer(GravityCompat.START)
        else drawer_layout?.closeDrawer(GravityCompat.START)
    }

    private fun updateNavDrawer() {
        childFragmentManager.executePendingTransactions()

        drawerFragment?.let { drawerFragment ->
            currentFragment?.let {
                when (it) {
                    is WholeSiteFeed -> drawerFragment.onScreenChanged(0)
                    is WorkoutFeed -> drawerFragment.onScreenChanged(1)
                    is FitnessEquipFeed -> drawerFragment.onScreenChanged(2)
                    is FitnessProgFeed -> drawerFragment.onScreenChanged(3)
                    is FitnessAdvicesFeed -> drawerFragment.onScreenChanged(4)
                    is HealthyFoodFeed -> drawerFragment.onScreenChanged(5)
                    is YoutubeGuidesFeed -> drawerFragment.onScreenChanged(6)
                    is UsefullThingsFeed -> drawerFragment.onScreenChanged(7)
                    is FavouriteListFragment -> drawerFragment.onScreenChanged(8)
                }
            }
        }
        openNavDrawer(false)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            openNavDrawer(false)
        } else {
            drawerFragment?.onBackPressed()
        }
    }

}
