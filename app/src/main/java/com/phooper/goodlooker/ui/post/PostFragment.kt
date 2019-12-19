package com.phooper.goodlooker.ui.post

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.post.PostPresenter
import com.phooper.goodlooker.presentation.post.PostView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.*
import com.phooper.goodlooker.util.shareText
import kotlinx.android.synthetic.main.fragment_feedlist.recycler_view
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.toolbar_post.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class PostFragment : BaseFragment(), PostView {

    override val layoutRes = R.layout.fragment_post

    @InjectPresenter
    lateinit var presenter: PostPresenter

    @ProvidePresenter
    fun providePresenter(): PostPresenter = PostPresenter(arguments?.getString(LINK))

    private val diffAdapter by lazy {
        DiffUtilCompositeAdapter.Builder()
            .add(H1ItemDelegateAdapter())
            .add(H2ItemDelegateAdapter())
            .add(H3ItemDelegateAdapter())
            .add(PItemDelegateAdapter())
            .add(MetaItemDelegateAdapter())
            .add(ImageItemDelegateAdapter { presenter.showImage(it) })
            .add(ButtonItemDelegateAdapter { showMessage(it.link) })
            .add(YoutubeItemDelegateAdapter{presenter.showVideo(it)})
            .add(ULItemDelegateAdapter())
            .add(OLItemDelegateAdapter())
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

        back_btn.setOnClickListener {
            presenter.onBackPressed()
        }

        share_btn.setOnClickListener {
            shareText(arguments?.getString(LINK))
        }

    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun fillList(list: List<IComparableItem>) {
        diffAdapter.swapData(list)
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    companion object {
        private const val LINK = "link"
        fun create(linkPost: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(LINK, linkPost)
                }
            }
    }
}