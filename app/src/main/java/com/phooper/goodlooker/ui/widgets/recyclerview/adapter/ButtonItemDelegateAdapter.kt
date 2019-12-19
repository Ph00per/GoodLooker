package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ButtonItemViewModel
import kotlinx.android.synthetic.main.item_button.*

class ButtonItemDelegateAdapter(private val onItemClick: ((ButtonItemViewModel) -> Unit)) :
    KDelegateAdapter<ButtonItemViewModel>() {

    override fun getLayoutId() = R.layout.item_button

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is ButtonItemViewModel

    override fun onBind(item: ButtonItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            hyper_link.text = item.text
            hyper_link.setOnClickListener { onItemClick(item) }
        }
    }
}