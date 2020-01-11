package com.phooper.goodlooker.presentation.search

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.db.AppDb
import com.phooper.goodlooker.db.dao.SearchHistoryDao
import com.phooper.goodlooker.db.entity.SearchHistory
import com.phooper.goodlooker.parser.Parser
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ConnectionRetryItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.LoadingItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.SearchFailedItemViewModel
import com.phooper.goodlooker.util.Constants
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class SearchPresenter : MvpPresenter<SearchView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var database: AppDb

    @Inject
    lateinit var searchHistoryDao: SearchHistoryDao

    init {
        App.daggerComponent.inject(this)
    }

    private var page = 0

    private val listFeed = mutableListOf<IComparableItem>()

    private var currentSearchText = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.enterInputMode()
        setHistoryList()
    }

    fun onBackPressed() {
        router.exit()
    }

    fun clearBtnClicked() {
        viewState.apply {
            setInputText("")
            enterInputMode()
        }
    }

    private fun setHistoryList() {
        CoroutineScope(Dispatchers.Main).launch {
            viewState.fillHistoryList(
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    searchHistoryDao.getAll()
                })
        }
    }

    fun enterPressed(searchText: String) {
        viewState.exitInputMode()
        if (searchText.isNotEmpty() && searchText != currentSearchText) {
            CoroutineScope(Dispatchers.IO).launch {
                searchHistoryDao.insert(SearchHistory(0, searchText))
                setHistoryList()
                withContext(Dispatchers.Main) {
                    setData(searchText, true)
                }
            }
        }
    }


    fun onTextChanged(inputText: CharSequence) {
        if (inputText.isNotEmpty()) {
            viewState.showClearBtn()
        } else {
            viewState.hideClearBtn()
        }
    }

    fun deleteHistoryItemClicked(id: Int, pos: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            searchHistoryDao.deleteById(id)
            withContext(Dispatchers.Main) {
                viewState.deleteHistoryListItem(pos)
            }
        }
    }

    fun historyItemSelected(name: String) {
        viewState.exitInputMode()
        CoroutineScope(Dispatchers.IO).launch {
            searchHistoryDao.insert(SearchHistory(0, name))
            setHistoryList()
            withContext(Dispatchers.Main) {
                viewState.setInputText(name)
                setData(name, true)
            }
        }
    }

    fun editTextFocusState(isFocused: Boolean) {
        if (isFocused) {
            viewState.enterInputMode()
        } else {
            viewState.exitInputMode()
        }
    }

    private fun setData(searchText: String, isItNewRequest: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isItNewRequest) {
                currentSearchText = searchText
                listFeed.clear()
                page = 1
                withContext(Dispatchers.Main) {
                    viewState.apply {
                        removeOnScrollListenerRV()
                        updateFeedList(listFeed)
                        startLoading()
                    }
                }
                repeat(5) {
                    try {
                        listFeed.addAll(parseData())
                        if (listFeed.isNotEmpty()) {
                            withContext(Dispatchers.Main) {
                                viewState.apply {
                                    updateFeedList(
                                        listFeed
                                    )
                                    stopLoading()
                                    addOnScrollListenerRV()
                                }
                            }
                        } else {
                            listFeed.add(SearchFailedItemViewModel())
                            withContext(Dispatchers.Main) {
                                viewState.apply {
                                    updateFeedList(
                                        listFeed
                                    )
                                    stopLoading()
                                }
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
                                    stopLoading()
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
        Parser().parseSearchFeed(
            Constants.BASE_URL + "page/" + page + "?s=" + currentSearchText
        )
    }

    fun onScrolled(dy: Int, total: Int?, lastVisibleItem: Int) {
        if ((dy > 0) && (lastVisibleItem + 1 == total)) {
            viewState.removeOnScrollListenerRV()
            setData(currentSearchText, false)
        }
    }

    fun onPostClicked(postLink: String) {
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
        setData(currentSearchText, listFeed.isEmpty())
    }
}