package com.phooper.goodlooker.ui.feedlist

import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.feedlist.FeedlistPresenter
import com.phooper.goodlooker.presentation.feedlist.FeedlistView
import com.phooper.goodlooker.ui.drawer.DrawerFlowFragment
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.adapters.ConnectionRetryItemDelegateAdapter
import com.phooper.goodlooker.adapters.LoadingItemDelegateAdapter
import com.phooper.goodlooker.adapters.PostItemDelegateAdapter
import kotlinx.android.synthetic.main.fragment_feedlist.*
import kotlinx.android.synthetic.main.toolbar_hamburger.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

abstract class FeedListFragment : BaseFragment(), FeedlistView {

    override val layoutRes = R.layout.fragment_feedlist

    abstract val siteCat: Int

    @InjectPresenter
    lateinit var presenter: FeedlistPresenter

    @ProvidePresenter
    fun providePresenter(): FeedlistPresenter = FeedlistPresenter(siteCat)

    private val diffAdapter by lazy {
        DiffUtilCompositeAdapter.Builder()
            .add(PostItemDelegateAdapter { item ->
                presenter.onRVItemClicked(item.linkPost)
            })
            .add(LoadingItemDelegateAdapter())
            .add(ConnectionRetryItemDelegateAdapter { presenter.retryConnection() })
            .build()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = diffAdapter
        }

        swipe_refresh_layout.apply {
            setColorSchemeColors(
                getColor(context, R.color.colorAccent),
                getColor(context, R.color.white)
            )
            setProgressBackgroundColorSchemeColor(getColor(context, R.color.colorPrimaryDark))
            setOnRefreshListener {
                presenter.pulledToRefresh()
            }
        }
        toolbar_menu_btn.setOnClickListener {
            (parentFragment as DrawerFlowFragment).openNavDrawer(true)
        }

        toolbar_search_btn.setOnClickListener {
            presenter.searchOnClicked()
        }
    }


    override fun startRefreshing() {
        swipe_refresh_layout.isRefreshing = true
    }

    override fun stopRefreshing() {
        swipe_refresh_layout.isRefreshing = false
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun setToolbarTitle(title: String) {
        toolbar_label.text = title
    }

    override fun scrollToBottom() {
        recycler_view.apply {
            adapter?.let {
                scrollToPosition(it.itemCount - 1)
            }
        }
    }

    override fun updateFeedList(listFeed: MutableList<IComparableItem>) {
        diffAdapter.swapData(listFeed)
    }

    override fun removeOnScrollListenerRV() {
        recycler_view.clearOnScrollListeners()
    }

    override fun addOnScrollListenerRV() {
        recycler_view.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
}

class WholeSiteFeed : FeedListFragment() {
    override val siteCat = 0
}

class WorkoutFeed : FeedListFragment() {
    override val siteCat = 1
}

class FitnessEquipFeed : FeedListFragment() {
    override val siteCat = 2
}

class FitnessProgFeed : FeedListFragment() {
    override val siteCat = 3
}

class FitnessAdvicesFeed : FeedListFragment() {
    override val siteCat = 4
}

class HealthyFoodFeed : FeedListFragment() {
    override val siteCat = 5
}

class YoutubeGuidesFeed : FeedListFragment() {
    override val siteCat = 6
}

class UsefullThingsFeed : FeedListFragment() {
    override val siteCat = 7
}
