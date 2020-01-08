package com.phooper.goodlooker.presentation.news.feedlist

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.parser.Parser
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ConnectionRetryItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.LoadingItemViewModel
import com.phooper.goodlooker.util.Constants.Companion.BASE_URL
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

    private val listFeed = mutableListOf<IComparableItem>()

    private val mapCategories =
        mapOf(
            0 to "uprazhneniya/page/",
            1 to "fitnes-inventar/page/",
            2 to "fitnes-programmy/page/",
            3 to "fitnes-sovety/page/",
            4 to "pitanie/page/",
            5 to "youtube-trenirovki/page/",
            6 to "poleznoe/page/"
        )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setData(true)
    }

    private fun setData(isItNewRequest: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isItNewRequest) {
                listFeed.clear()
                page = 1
                withContext(Dispatchers.Main) {
                    viewState.apply {
                        removeOnScrollListenerRV()
                        startRefreshing()
                    }
                }
                repeat(5) {
                    try {
                        listFeed.addAll(parseData())
                        withContext(Dispatchers.Main) {
                            viewState.apply {
                                updateFeedList(
                                    listFeed
                                )
                                stopRefreshing()
                                addOnScrollListenerRV()
                            }
                        }
                        return@launch
                    } catch (e: Exception) {
                        if (it != 4) {
                            delay(1000)
                        } else {
                            --page
                            withContext(Dispatchers.Main) {
                                viewState.apply {
                                    stopRefreshing()
                                    updateFeedList(listFeed)
                                }
                                showConnectionProblems()
                            }
                            return@launch
                        }
                    }
                }
            } else {
                listFeed.add(LoadingItemViewModel())
                val indexOfLoadingElement = listFeed.size - 1
                ++page
                withContext(Dispatchers.Main) {
                    viewState.apply {
                        updateFeedList(listFeed)
                        scrollToBottom()
                    }
                }
                repeat(5) {
                    try {
                        listFeed.apply {
                            addAll(parseData())
                            removeAt(indexOfLoadingElement)
                        }
                        withContext(Dispatchers.Main) {
                            viewState.apply {
                                updateFeedList(
                                    listFeed
                                )
                                addOnScrollListenerRV()
                            }

                        }
                        return@launch
                    } catch (e: org.jsoup.HttpStatusException) {
                        withContext(Dispatchers.Main) {
                            listFeed.removeAt(indexOfLoadingElement)
                            viewState.apply {
                                updateFeedList(listFeed)
                                showMessage("Вы посмотрели все публикации!")
                            }
                        }
                        return@launch
                    } catch (e: Exception) {
                        if (it != 4) {
                            delay(1000)
                        } else {
                            --page
                            withContext(Dispatchers.Main) {
                                listFeed.removeAt(indexOfLoadingElement)
                                viewState.updateFeedList(listFeed)
                                showConnectionProblems()
                            }
                            return@launch
                        }
                    }
                }
            }
        }
    }


    private suspend fun parseData() = withContext(Dispatchers.IO) {
        Parser().parseFeed(
            BASE_URL + "category/" + mapCategories.getValue(
                siteCat
            ) + page
        )
    }


    fun onScrolled(dy: Int, total: Int?, lastVisibleItem: Int) {
        if ((dy > 0) && (lastVisibleItem + 1 == total)) {
            viewState.removeOnScrollListenerRV()
            setData(false)
        }
    }

    fun onRVItemClicked(postLink: String) {
        router.navigateTo(Screens.Post(postLink))
    }

    private fun showConnectionProblems() {
        listFeed.add(ConnectionRetryItemViewModel())
        viewState.apply {
            removeOnScrollListenerRV()
            updateFeedList(listFeed)
        }
    }

    fun retryConnection() {
        listFeed.removeAt(listFeed.size - 1)
        viewState.updateFeedList(listFeed)
        setData(listFeed.isEmpty())
    }

    fun pulledToRefresh() {
        setData(true)
    }
}


