package com.phooper.goodlooker.ui.global.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.ButtonItemViewModel
import com.phooper.goodlooker.util.underline
import kotlinx.android.synthetic.main.item_button.*

class ButtonItemDelegateAdapter(private val onItemClick: ((String) -> Unit)) :
    KDelegateAdapter<ButtonItemViewModel>() {

    override fun getLayoutId() = R.layout.item_button

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is ButtonItemViewModel

    override fun onBind(item: ButtonItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.apply {
                text = item.text
                underline()
                setOnClickListener { onItemClick(item.link) }
            }
        }
    }
}