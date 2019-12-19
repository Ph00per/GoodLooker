package com.phooper.goodlooker.presentation.news.feedlist

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.parser.Parser
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ConnectionRetryItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.LoadingItemViewModel
import kotlinx.coroutines.*
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

    private var page = 0

    private val listNews = mutableListOf<IComparableItem>()

    private val mapCategories =
        mapOf(
            0 to "uprazhneniya/page/",
            1 to "fitnes-inventar/page/",
            2 to "fitnes-programmy/page/",
            3 to "sportpit/page/",
            4 to "pitanie/page/",
            5 to "youtube-trenirovki/page/",
            6 to "/sportivnaya-odezhda/page/"
        )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshData()
    }

    fun refreshData() {
        viewState.apply {
            startRefreshing()
            removeOnScrollListenerRV()
        }
        listNews.clear()
        page = 1

        CoroutineScope(Dispatchers.IO).launch {
            val result = async { getData() }
            listNews.addAll(result.await())
            withContext(Dispatchers.Main) {
                viewState.apply {
                    stopRefreshing()
                    updateFeedList(listNews)
                    addOnScrollListenerRV()
                }
            }
        }
    }

    private fun loadMoreData(itemCount: Int) {
        viewState.apply {
            removeOnScrollListenerRV()
            listNews.add(itemCount, LoadingItemViewModel())
            updateFeedList(listNews)
            scrollToPos(itemCount)
        }
        ++page
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { getData() }
            listNews.addAll(result.await())
            withContext(Dispatchers.Main) {
                viewState.apply {
                    listNews.removeAt(itemCount)
                    if (itemCount != listNews.size) addOnScrollListenerRV()
                    delay(500)
                    updateFeedList(listNews)
                }
            }
        }
    }

    private suspend fun getData(): List<IComparableItem> {
        try {
            return Parser().parseNews(
                "https://goodlooker.ru/category/" + mapCategories.getValue(
                    siteCat
                ) + page
            )
        } catch (e: org.jsoup.HttpStatusException) {
            CoroutineScope(Dispatchers.Main).launch {
                viewState.apply {
                    showMessage("Вы достигли конца! :)")
                }
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                showConnectionProblems()
            }
            --page
        }
        return emptyList()
    }

    fun onScrolled(dy: Int, total: Int?, lastVisibleItem: Int) {
//            Log.d("total: $total", " last: ${lastVisibleItem + 1}")

        when {
            (dy > 0) -> {
                if (lastVisibleItem + 1 == total) {
                    loadMoreData(total)
                }
            }

            (dy < 0) -> {

            }
        }
    }

    fun onRVItemClicked(link: String) {
        router.navigateTo(Screens.Post(link))
    }

    private fun showConnectionProblems() {
        listNews.add(ConnectionRetryItemViewModel())
        viewState.apply {
            updateFeedList(listNews)
            scrollToPos(listNews.count())
        }
    }


    fun retryConnection() {
        listNews.removeAt(listNews.size - 1)
        viewState.updateFeedList(listNews)
        loadMoreData(listNews.size)
    }
}


