package com.phooper.goodlooker.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.R
import com.phooper.goodlooker.db.entity.SearchHistory
import com.phooper.goodlooker.presentation.search.SearchPresenter
import com.phooper.goodlooker.presentation.search.SearchView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.*
import com.phooper.goodlooker.util.hideKeyboard
import com.phooper.goodlooker.util.openKeyboard
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toolbar_search.*
import moxy.presenter.InjectPresenter

class SearchFragment : BaseFragment(), SearchView {

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    override val layoutRes = R.layout.fragment_search

    private val historyAdapter = SearchHistoryItemAdapter()

    private val diffAdapter by lazy {
        DiffUtilCompositeAdapter.Builder()
            .add(PostItemDelegateAdapter { item ->
                presenter.onRVItemClicked(item.linkPost)
            })
            .add(LoadingItemDelegateAdapter())
            .add(ConnectionRetryItemDelegateAdapter { presenter.retryConnection() })
            .add(SearchFailedItemDelegateAdapter())
            .build()
    }

    override fun enterInputMode() {
        edit_text_search?.openKeyboard()

        history_rv.visibility = View.VISIBLE
        feed_rv.visibility = View.GONE
    }

    override fun exitInputMode() {
        edit_text_search?.hideKeyboard()

        feed_rv.visibility = View.VISIBLE
        history_rv.visibility = View.GONE
    }

    override fun setInputText(text: String) {
        edit_text_search?.setText(text)
    }

    override fun showClearBtn() {
        clear_input_btn?.visibility = View.VISIBLE
    }

    override fun hideClearBtn() {
        clear_input_btn?.visibility = View.GONE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        edit_text_search?.apply {
            setOnFocusChangeListener { _, isFocused ->
                presenter.editTextFocusState(isFocused)
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                    presenter.onTextChanged(text)
                }
            })

            setOnKeyListener { _, _, keyEvent ->
                if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                    presenter.enterPressed(
                        edit_text_search.text.toString())
                }
                false
            }
        }
        clear_input_btn.setOnClickListener {
            presenter.clearBtnClicked()
        }
        back_btn.setOnClickListener {
            presenter.onBackPressed()
        }
        history_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter.apply {
                onDeleteBtnClick = { id, pos ->
                    presenter.deleteHistoryItemClicked(id, pos)
                }
                onItemClick = { name ->
                    presenter.historyItemSelected(name)
                }
            }
            feed_rv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = diffAdapter
            }
        }
    }

    override fun deleteHistoryListItem(position: Int) {
        historyAdapter.removeItem(position)
    }

    override fun fillHistoryList(historyList: List<SearchHistory>) {
        historyAdapter.setData(historyList)
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun startLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun removeOnScrollListenerRV() {
        feed_rv.clearOnScrollListeners()
    }

    override fun addOnScrollListenerRV() {
        feed_rv.apply {
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    presenter.onScrolled(
                        dy,
                        layoutManager?.itemCount,
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    )
                }
            })
        }
    }

    override fun updateFeedList(listFeed: MutableList<IComparableItem>) {
        diffAdapter.swapData(listFeed)
    }

    override fun scrollToBottom() {
        feed_rv.apply {
            adapter?.let {
                scrollToPosition(it.itemCount - 1)
            }
        }
    }
}