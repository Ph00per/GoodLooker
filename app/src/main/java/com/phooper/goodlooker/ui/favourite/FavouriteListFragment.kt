package com.phooper.goodlooker.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.global.adapters.FavouritePostItemAdapter
import com.phooper.goodlooker.entity.FavouritePost
import com.phooper.goodlooker.presentation.favourite.FavouriteListPresenter
import com.phooper.goodlooker.presentation.favourite.FavouriteListView
import com.phooper.goodlooker.ui.drawer.DrawerFlowFragment
import com.phooper.goodlooker.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_favourite_list.*
import kotlinx.android.synthetic.main.toolbar_hamburger.*
import moxy.presenter.InjectPresenter

class FavouriteListFragment : BaseFragment(), FavouriteListView {

    override val layoutRes = R.layout.fragment_favourite_list

    private val favouritePostAdapter = FavouritePostItemAdapter()

    @InjectPresenter
    lateinit var presenter: FavouriteListPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
            adapter = favouritePostAdapter.apply {
                onItemClick = { link ->
                    presenter.postClicked(link)
                }
            }
        }
        toolbar_menu_btn.setOnClickListener {
            (parentFragment as DrawerFlowFragment).openNavDrawer(true)
        }

        toolbar_search_btn.setOnClickListener {
            presenter.searchOnClicked()
        }
    }

    override fun showNothingFound() {
        recycler_view.visibility = View.GONE
        no_favourites_layout.visibility = View.VISIBLE
    }

    override fun hideNothingFound() {
        recycler_view.visibility = View.VISIBLE
        no_favourites_layout.visibility = View.GONE
    }

    override fun updateAndShowRecyclerList(listPosts: List<FavouritePost>) {
        favouritePostAdapter.setData(listPosts)
    }

    override fun setToolbarTitle(title: String) {
        toolbar_label.text = title
    }

}