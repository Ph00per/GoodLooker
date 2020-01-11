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

    private var currentCategory = 0

    private val mapScreens = mapOf(
        0 to Screens.Workout,
        1 to Screens.FitnessEquip,
        2 to Screens.FitnessProg,
        3 to Screens.FitnessAdvices,
        4 to Screens.HealthyFood,
        5 to Screens.YoutubeGuides,
        6 to Screens.UsefullThings
    )

    private val currentScreens = mutableListOf<Screen>(Screens.Workout)

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




