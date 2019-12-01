package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.bumptech.glide.Glide
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.NewsItemViewModel
import kotlinx.android.synthetic.main.item_news.*

class NewsItemDelegateAdapter(private val onItemClick: ((NewsItemViewModel) -> Unit)) :
    KDelegateAdapter<NewsItemViewModel>() {

    override fun getLayoutId() = R.layout.item_news

    override fun onBind(item: NewsItemViewModel, viewHolder: KViewHolder) =

        with(viewHolder) {

            item_layout.setOnClickListener { onItemClick(item) }
            post_title.text = item.title
            post_date.text = item.date

            //TODO DO this in presenter
            Glide.with(itemView.context)
                .load(item.linkImage)
                .into(post_img)

            post_views.text = item.views
            post_comments.text = item.comments

        }

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is NewsItemViewModel
}