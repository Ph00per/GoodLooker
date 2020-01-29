package com.phooper.goodlooker.presentation.search

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.entity.ConnectionRetryItemViewModel
import com.phooper.goodlooker.entity.LoadingItemViewModel
import com.phooper.goodlooker.entity.SearchFailedItemViewModel
import com.phooper.goodlooker.model.interactor.SearchInteractor
import com.phooper.goodlooker.util.Constants
import com.phooper.goodlooker.util.Constants.Companion.NO_MORE_CONTENT_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class SearchPresenter : MvpPresenter<SearchView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var searchInteractor: SearchInteractor

    init {
        App.daggerComponent.inject(this)
    }

    private var currentPage = 0

    private val currentListFeed = mutableListOf<IComparableItem>()

    private var currentSearchText = ""

    private var indexOfLoadingElement = 0

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
                withContext(CoroutineScope(IO).coroutineContext) {
                    searchInteractor.getSearchHistory()
                })
        }
    }

    fun enterPressed(searchText: String) {
        viewState.exitInputMode()
        if (searchText.isNotEmpty() && searchText != currentSearchText) {
            currentSearchText = searchText
            CoroutineScope(IO).launch {
                searchInteractor.addNewSearchRequest(searchText)
                setHistoryList()
                setNewFeedData()
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
        CoroutineScope(IO).launch {
            searchInteractor.deleteSearchRequestById(id)
            withContext(Dispatchers.Main) {
                viewState.deleteHistoryListItem(pos)
            }
        }
    }

    fun historyItemSelected(name: String) {
        viewState.exitInputMode()
        if (name != currentSearchText) {
            currentSearchText = name
            CoroutineScope(IO).launch {
                searchInteractor.addNewSearchRequest(name)
                setHistoryList()
                withContext(Dispatchers.Main) {
                    viewState.setInputText(name)
                }
                setNewFeedData()
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

    private suspend fun setNewFeedData() {
        currentListFeed.clear()
        currentPage = 1
        withContext(Dispatchers.Main) {
            viewState.updateFeedList(currentListFeed)
            enterLoadingNewState()
        }
        searchInteractor.getFeedBySearchTextAndPage(currentSearchText, currentPage).let {
            if (it.isSuccessful) {
                currentListFeed.addAll(it.listContent!!)
            } else {
                when (it.error) {
                    NO_MORE_CONTENT_ERROR -> {
                        withContext(Dispatchers.Main) {
                            showNothingFound()
                        }
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            showConnectionProblems()
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) { exitLoadingNewState() }
        }
    }

    private suspend fun setNextPageFeedData() {
        ++currentPage
        withContext(Dispatchers.Main) { enterLoadingMoreState() }
        searchInteractor.getFeedBySearchTextAndPage(currentSearchText, currentPage).let {
            if (it.isSuccessful) {
                currentListFeed.addAll(it.listContent!!)
                withContext(Dispatchers.Main) {
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
                        withContext(Dispatchers.Main) {
                            viewState.apply {
                                showMessage("Вы посмотрели все публикации!")
                            }
                        }
                    }
                    else -> {
                        --currentPage
                        withContext(Dispatchers.Main) {
                            showConnectionProblems()
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) { exitLoadingMoreState() }
        }
    }

    fun onScrolled(dy: Int, total: Int?, lastVisibleItem: Int) {
        if ((dy > 0) && (lastVisibleItem + 1 == total)) {
            viewState.removeOnScrollListenerRV()
            CoroutineScope(IO).launch { setNextPageFeedData() }
        }
    }

    fun onPostClicked(postLink: String) {
        router.navigateTo(Screens.Post(postLink))
    }

    fun retryConnection() {
        currentListFeed.removeAt(currentListFeed.size - 1)
        viewState.updateFeedList(currentListFeed)
        CoroutineScope(IO).launch {
            if (currentListFeed.isEmpty()) {
                setNewFeedData()
            } else {
                setNextPageFeedData()
            }
        }
    }

    private fun showConnectionProblems() {
        currentListFeed.add(ConnectionRetryItemViewModel())
        viewState.apply {
            removeOnScrollListenerRV()
        }
    }

    private fun showNothingFound() {
        currentListFeed.add(SearchFailedItemViewModel())
        viewState.apply {
            removeOnScrollListenerRV()
        }
    }


    private fun enterLoadingNewState() {
        viewState.apply {
            removeOnScrollListenerRV()
            startLoading()
        }
    }

    private fun exitLoadingNewState() {
        viewState.apply {
            stopLoading()
            updateFeedList(currentListFeed)
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
}