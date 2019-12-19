package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.bumptech.glide.RequestManager
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.PostItemViewModel
import kotlinx.android.synthetic.main.item_news.*
import javax.inject.Inject

class PostItemDelegateAdapter(private val onItemClick: ((PostItemViewModel) -> Unit)) :
    KDelegateAdapter<PostItemViewModel>() {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var requestManager: RequestManager

    override fun getLayoutId() = R.layout.item_news

    override fun onBind(item: PostItemViewModel, viewHolder: KViewHolder) =
        with(viewHolder) {
            item_layout.setOnClickListener { onItemClick(item) }
            post_title.text = item.title
            post_date.text = item.date

            requestManager
                .load(item.imageLink)
                .into(post_img)

            post_views.text = item.views
            post_comments.text = item.comments

        }

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is PostItemViewModel
}