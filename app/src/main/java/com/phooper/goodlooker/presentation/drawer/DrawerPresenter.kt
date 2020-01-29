package com.phooper.goodlooker.presentation.drawer

import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import javax.inject.Inject

@InjectViewState
class DrawerPresenter : MvpPresenter<DrawerView>() {

    @Inject
    lateinit var router: Router

    init {
        App.localNavigationComponent.inject(this)
    }

    private var safetyExit = 1

    private val mapScreens = mapOf(
        0 to Screens.WholeSite,
        1 to Screens.Workout,
        2 to Screens.FitnessEquip,
        3 to Screens.FitnessProg,
        4 to Screens.FitnessAdvices,
        5 to Screens.HealthyFood,
        6 to Screens.YoutubeGuides,
        7 to Screens.UsefullThings,
        8 to Screens.FavouriteList
    )

    private val currentScreens = mutableListOf<Screen>(Screens.WholeSite)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.apply {


        }
    }


    fun menuItemClicked(category: Int) {
        mapScreens.getValue(category).let { requestedScreen
            ->
            if (currentScreens.contains(
                    requestedScreen
                )
            ) {
                viewState.selectMenuItem(category)
                router.backTo(
                    requestedScreen

                )
                //Removing all screens with higher index than requested screen index
                currentScreens.filter {
                    currentScreens.indexOf(it) <= currentScreens.indexOf(requestedScreen)
                }.let {
                    currentScreens.apply {
                        clear()
                        addAll(it)
                    }
                }
            } else {
                viewState.selectMenuItem(category)
                router.navigateTo(
                    requestedScreen
                )
                if (requestedScreen == Screens.FavouriteList) {
                    return
                }
                currentScreens.add(
                    requestedScreen
                )
            }
        }
    }

    fun onBackPressed() {
        if (currentScreens.size == 1) {
            if (safetyExit == 1) {
                viewState.showMessage("Нажмите еще раз для выхода")
                safetyExit--
                CoroutineScope(Dispatchers.IO).launch {
                    delay(2000)
                    safetyExit++
                }
            } else {
                router.exit()
            }
        } else {
            currentScreens.removeAt(currentScreens.size - 1)
            router.exit()
        }
    }

    fun onScreenChanged(index: Int) {
        viewState.selectMenuItem(index)
    }
}




