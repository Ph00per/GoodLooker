package com.phooper.goodlooker.ui.global.adapters

import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.ULItemViewModel
import com.phooper.goodlooker.util.makeLinks
import kotlinx.android.synthetic.main.item_h1.*

class ULItemDelegateAdapter(private val onLinkClick: ((String) -> Unit)) : KDelegateAdapter<ULItemViewModel>() {

    override fun getLayoutId() = R.layout.item_ul_li

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is ULItemViewModel

    override fun onBind(item: ULItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.text
            item.hyperlinks?.forEach { hyperlink ->
                text.makeLinks(Pair(hyperlink.text, View.OnClickListener {
                    onLinkClick(hyperlink.link)
                }))
            }
        }
    }
}