package com.phooper.goodlooker.ui.news.feedlist

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.news.feedlist.FeedlistPresenter
import com.phooper.goodlooker.presentation.news.feedlist.FeedlistView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.ConnectionRetryItemDelegateAdapter
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.LoadingItemDelegateAdapter
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.PostItemDelegateAdapter
import kotlinx.android.synthetic.main.fragment_feedlist.*
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
                presenter.refreshData()
            }
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

    override fun scrollToPos(pos: Int) {
        recycler_view.scrollToPosition(pos)
    }

    override fun updateFeedList(listFeed: MutableList<IComparableItem>) {
        diffAdapter.swapData(listFeed)
    }

    override fun removeOnScrollListenerRV() {
        recycler_view.clearOnScrollListeners()
    }

    override fun addOnScrollListenerRV() {
        recycler_view.apply {
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
}

class WorkoutFeed : FeedListFragment() {
    override val siteCat = 0

    companion object {
        fun newInstance(bundle: Bundle? = null) = WorkoutFeed().apply { arguments = bundle }
    }

}

class FitnessEquipFeed : FeedListFragment() {
    override val siteCat = 1

    companion object {
        fun newInstance(bundle: Bundle? = null) = FitnessEquipFeed().apply { arguments = bundle }
    }
}

class FitnessProgFeed : FeedListFragment() {
    override val siteCat = 2

    companion object {
        fun newInstance(bundle: Bundle? = null) = FitnessProgFeed().apply { arguments = bundle }
    }
}

class SportFoodFeed : FeedListFragment() {
    override val siteCat = 3

    companion object {
        fun newInstance(bundle: Bundle? = null) = SportFoodFeed().apply { arguments = bundle }
    }
}

class HealthyFoodFeed : FeedListFragment() {
    override val siteCat = 4

    companion object {
        fun newInstance(bundle: Bundle? = null) = HealthyFoodFeed().apply { arguments = bundle }
    }
}

class YoutubeTrainFeed : FeedListFragment() {
    override val siteCat = 5

    companion object {
        fun newInstance(bundle: Bundle? = null) = YoutubeTrainFeed().apply { arguments = bundle }
    }
}

class SportClothesFeed : FeedListFragment() {
    override val siteCat = 6

    companion object {
        fun newInstance(bundle: Bundle? = null) = SportClothesFeed().apply { arguments = bundle }
    }
}
