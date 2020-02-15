package com.phooper.goodlooker.presentation.feedlist

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.entity.ConnectionRetryItemViewModel
import com.phooper.goodlooker.entity.LoadingItemViewModel
import com.phooper.goodlooker.model.interactor.FeedInteractor
import com.phooper.goodlooker.util.Constants.Companion.NO_MORE_CONTENT_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class FeedlistPresenter(private val siteCat: Int) : MvpPresenter<FeedlistView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var feedInteractor: FeedInteractor

    private var indexOfLoadingElement = 0

    private var currentPage = 0

    private val currentListFeed = mutableListOf<IComparableItem>()

    private val mapToolbarTitles =
        mapOf(
            0 to "Все публикации",
            1 to "Упражнения",
            2 to "Фитнес - инвентарь",
            3 to "Фитнес - программы",
            4 to "Фитнес - советы",
            5 to "Питание",
            6 to "Youtube - тренировки",
            7 to "Полезное"
        )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setToolbarTitle(mapToolbarTitles.getValue(siteCat))
        CoroutineScope(IO).launch { setNewData() }
    }

    private suspend fun setNewData() {
        currentListFeed.clear()
        currentPage = 1
        withContext(Main) { enterLoadingNewState() }
        feedInteractor.getFeedByCategoryAndPage(siteCat, currentPage).let {
            if (it.isSuccessful) {
                currentListFeed.addAll(it.listContent!!)
                withContext(Main) {
                    viewState.updateFeedList(
                        currentListFeed
                    )
                }
            } else {
                withContext(Main) {
                    showConnectionProblems()
                }
            }
            withContext(Main) { exitLoadingNewState() }
        }
    }

    private suspend fun setNextPageData() {
        ++currentPage
        withContext(Main) { enterLoadingMoreState() }
        feedInteractor.getFeedByCategoryAndPage(siteCat, currentPage).let {
            if (it.isSuccessful) {
                currentListFeed.addAll(it.listContent!!)
                withContext(Main) {
                    viewState.apply {
                        updateFeedList(
                            currentListFeed
                        )
                        addOnScrollListenerRV()
                    }
                }
            } else {
                when (it.error) {
                    NO_MORE_CONTENT_ERROR -> {
                        withContext(Main) {
                            viewState.apply {
                                showMessage("Вы посмотрели все публикации!")
                            }
                        }
                    }
                    else -> {
                        --currentPage
                        withContext(Main) {
                            showConnectionProblems()
                        }
                    }
                }
            }
            withContext(Main) { exitLoadingMoreState() }
        }
    }


    private fun enterLoadingNewState() {
        viewState.apply {
            removeOnScrollListenerRV()
            startRefreshing()
        }
    }


    private fun exitLoadingNewState() {
        viewState.apply {
            stopRefreshing()
            addOnScrollListenerRV()
        }
    }

    private fun enterLoadingMoreState() {
        currentListFeed.add(LoadingItemViewModel())
        indexOfLoadingElement = currentListFeed.lastIndex
        viewState.apply {
            updateFeedList(currentListFeed)
            scrollToBottom()
        }
    }

    private fun exitLoadingMoreState() {
        currentListFeed.removeAt(indexOfLoadingElement)
        viewState.apply {
            updateFeedList(currentListFeed)
        }
    }

    private fun showConnectionProblems() {
        currentListFeed.add(ConnectionRetryItemViewModel())
        viewState.apply {
            removeOnScrollListenerRV()
            updateFeedList(currentListFeed)
        }
    }

    fun retryConnection() {
        currentListFeed.removeAt(currentListFeed.lastIndex)
        viewState.updateFeedList(currentListFeed)
        CoroutineScope(IO).launch {
            if (currentListFeed.isEmpty()) {
                setNewData()
            } else {
                setNextPageData()
            }
        }
    }

    fun pulledToRefresh() {
        CoroutineScope(IO).launch { setNewData() }
    }

    fun searchOnClicked() {
        router.navigateTo(Screens.Search)
    }

    fun onScrolled(dy: Int, total: Int?, lastVisibleItem: Int) {
        if ((dy > 0) && (lastVisibleItem + 1 == total)) {
            viewState.removeOnScrollListenerRV()
            CoroutineScope(IO).launch { setNextPageData() }
        }
    }

    fun onRVItemClicked(postLink: String) {
        router.navigateTo(Screens.Post(postLink))
    }
}
