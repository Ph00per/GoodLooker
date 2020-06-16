package com.phooper.goodlooker.ui.global.adapters

import com.bumptech.glide.RequestManager
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.ImageItemViewModel
import kotlinx.android.synthetic.main.item_image.*
import javax.inject.Inject

class ImageItemDelegateAdapter(private val onItemClick: ((String) -> Unit)) :
    KDelegateAdapter<ImageItemViewModel>() {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var requestManager: RequestManager

    override fun getLayoutId() = R.layout.item_image

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is ImageItemViewModel

    override fun onBind(item: ImageItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            requestManager
                .load(item.link)
                .into(image)
            image.setOnClickListener {
                onItemClick(item.link)
            }
        }
    }
}