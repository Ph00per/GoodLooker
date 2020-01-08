package com.phooper.goodlooker.presentation.news

import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.ui.news.feedlist.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class NewsPresenter : MvpPresenter<NewsView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    private var safetyExit = 1

    private var currentCategory = 0

    private val mapCategory = mapOf(
        0 to WorkoutFeed.newInstance(),
        1 to FitnessEquipFeed.newInstance(),
        2 to FitnessProgFeed.newInstance(),
        3 to AdvicesFeed.newInstance(),
        4 to HealthyFoodFeed.newInstance(),
        5 to YoutubeTrainFeed.newInstance(),
        6 to UsefullFeed.newInstance()
    )

    private val mapIcons = mapOf(
        0 to R.drawable.ic_workout,
        1 to R.drawable.ic_equipment,
        2 to R.drawable.ic_training_program,
        3 to R.drawable.ic_paper,
        4 to R.drawable.ic_healthy_food,
        5 to R.drawable.ic_youtube,
        6 to R.drawable.ic_heart
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.apply {
            changeToolBarIcon(mapIcons.getValue(currentCategory))
            changeFeedList(mapCategory.getValue(currentCategory))
        }
    }

    fun toolbarItemSelected(pos: Int) {
        if (currentCategory != pos) {
            currentCategory = pos
            viewState.apply {
                changeToolBarIcon(mapIcons.getValue(currentCategory))
                changeFeedList(mapCategory.getValue(currentCategory))
            }
        }
    }

    fun onBackPressed() {
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
    }

    fun searchButtonClicked() {
        router.navigateTo(Screens.Search)
    }
}




