package com.phooper.goodlooker.ui.news

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.news.NewsPresenter
import com.phooper.goodlooker.presentation.news.NewsView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.ui.news.feedlist.FeedListFragment
import kotlinx.android.synthetic.main.toolbar_categories.*
import moxy.presenter.InjectPresenter

class NewsFragment : BaseFragment(), NewsView {
    override val layoutRes = R.layout.fragment_news

    @InjectPresenter
    lateinit var presenter: NewsPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        //Spinner init
        drop_down_menu.apply {
            adapter = ArrayAdapter(
                context,
                R.layout.dropdown_menu_style,
                resources.getStringArray(R.array.site_categories_array)
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.toolbarItemSelected(position)
                }
            }
        }

        //Search btn init
        search_btn.setOnClickListener {
            presenter.searchButtonClicked()
        }
    }

    override fun changeToolBarIcon(resId: Int) {
        toolbar_icon.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                resId
            )
        )
    }

    override fun changeFeedList(targetFragment: FeedListFragment) {

        with(childFragmentManager.beginTransaction()) {
            childFragmentManager.fragments.filter { it != targetFragment }.forEach {
                hide(it)
                it.userVisibleHint =
                    false
            }
            targetFragment.let {
                if (it.isAdded) {
                    show(it)
                } else add(R.id.rv_container, it)
                it.userVisibleHint = true
            }
            commit()
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}