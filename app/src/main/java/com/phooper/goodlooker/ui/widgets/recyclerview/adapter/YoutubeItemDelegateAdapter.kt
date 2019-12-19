package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.bumptech.glide.RequestManager
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.YoutubeItemViewModel
import kotlinx.android.synthetic.main.item_youtube.*
import javax.inject.Inject

class YoutubeItemDelegateAdapter(private val onItemClick: ((String) -> Unit)) :
    KDelegateAdapter<YoutubeItemViewModel>() {


    @Inject
    lateinit var requestManager: RequestManager

    init {
        App.daggerComponent.inject(this)
    }

    override fun getLayoutId() = R.layout.item_youtube

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is YoutubeItemViewModel

    override fun onBind(item: YoutubeItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            requestManager
                .load("https://img.youtube.com/vi/${item.videoCode}/0.jpg")
                .into(preview_image)
            whole_item.setOnClickListener {
                onItemClick(item.videoCode)
            }
        }
    }
}


