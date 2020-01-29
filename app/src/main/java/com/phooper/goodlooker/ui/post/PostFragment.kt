package com.phooper.goodlooker.ui.post

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.muddzdev.styleabletoast.StyleableToast
import com.phooper.goodlooker.R
import com.phooper.goodlooker.adapters.*
import com.phooper.goodlooker.adapters.adapter.H2ItemDelegateAdapter
import com.phooper.goodlooker.adapters.adapter.H3ItemDelegateAdapter
import com.phooper.goodlooker.presentation.post.PostPresenter
import com.phooper.goodlooker.presentation.post.PostView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.phooper.goodlooker.util.openInBrowser
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
    fun providePresenter(): PostPresenter = PostPresenter(arguments?.getString(LINK)!!)

    private val diffAdapter by lazy {
        DiffUtilCompositeAdapter.Builder()
            .add(H1ItemDelegateAdapter())
            .add(H2ItemDelegateAdapter())
            .add(H3ItemDelegateAdapter())
            .add(PItemDelegateAdapter { presenter.linkClicked(it) })
            .add(MetaItemDelegateAdapter())
            .add(ImageItemDelegateAdapter { presenter.showImage(it) })
            .add(ButtonItemDelegateAdapter { presenter.linkClicked(it) })
            .add(YoutubeItemDelegateAdapter { presenter.showVideo(it) })
            .add(ULItemDelegateAdapter { presenter.linkClicked(it) })
            .add(OLItemDelegateAdapter { presenter.linkClicked(it) })
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

        back_btn.setOnClickListener {
            presenter.onBackPressed()
        }

        share_btn.setOnClickListener {
            shareText(arguments?.getString(LINK))
        }

        favour_btn.setOnClickListener {
            presenter.favourBtnOnClicked()
        }
    }

    override fun openBrowserLink(link: String?) {
        openInBrowser(link)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun showMessage(msg: String) {
        StyleableToast.makeText(context!!, msg, R.style.toast).show()
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

    override fun changeFavouriteBtn(resId: Int) {
        favour_btn.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                resId
            )
        )
    }

    companion object {
        private const val LINK = "link"
        fun create(postLink: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(LINK, postLink)
                }
            }
    }
}